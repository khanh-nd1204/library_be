package com.project.library.controller;

import com.project.library.dto.ResponseObject;
import com.project.library.dto.auth.*;
import com.project.library.dto.mail.ResendMailDTO;
import com.project.library.dto.user.*;
import com.project.library.entity.UserEntity;
import com.project.library.service.SecurityService;
import com.project.library.service.UserService;
import com.project.library.util.NotFoundException;
import com.project.library.util.UnauthorizedException;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/auth")
public class AuthCtrl {

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserService userService;

    @Value("${jwt.access-token-validity-in-seconds}")
    private Long accessTokenValidityInSeconds;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private Long refreshTokenValidityInSeconds;

    @PostMapping("/login")
    public ResponseEntity<ResponseObject> login(@Valid @RequestBody AuthLoginDTO authLoginDTO) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(authLoginDTO.getUsername(), authLoginDTO.getPassword());
            Authentication authentication
                    = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            AuthDTO authDTO = new AuthDTO();
            UserEntity user = userService.getUserByEmail(authLoginDTO.getUsername());
            if (!user.isActive()) {
                ResponseObject res = new ResponseObject(HttpStatus.NOT_FOUND.value(),
                        "User is inactive", null, "Not found");
                return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
            }
            authDTO.setUser(convertDTO(user));

            String accessToken = securityService.createToken(authDTO.getUser(), accessTokenValidityInSeconds);
            authDTO.setAccessToken(accessToken);

            String refreshToken = securityService.createToken(authDTO.getUser(), refreshTokenValidityInSeconds);
            userService.updateUserToken(user.getId(), refreshToken);
            ResponseCookie springCookie = ResponseCookie
                    .from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(refreshTokenValidityInSeconds)
                    .build();

            ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                    "Login successfully", authDTO, null
            );
            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.SET_COOKIE, springCookie.toString())
                    .body(res);
        } catch (Exception e) {
            throw new UnauthorizedException(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<ResponseObject> getUserInfo() {
        String email = SecurityService.getCurrentUserLogin()
                .orElseThrow(() -> new UnauthorizedException("Invalid token"));

        UserEntity user = userService.getUserByEmail(email);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "User fetched successfully", convertDTO(user), null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/refresh")
    public ResponseEntity<ResponseObject> getRefreshToken(
            @CookieValue(name = "refreshToken") String refreshToken) throws Exception {
        System.out.println(refreshToken);
        if (refreshToken == null) {
            throw new BadRequestException("Invalid refresh token");
        }

        Jwt decodedToken = securityService.checkValidRefreshToken(refreshToken);
        String email = decodedToken.getSubject();

        UserEntity user = userService.getUserByEmailAndRefreshToken(email, refreshToken);
        if (user == null) {
            throw new BadRequestException("Invalid refresh token");
        }

        AuthDTO authDTO = new AuthDTO();
        authDTO.setUser(convertDTO(user));

        String accessToken = securityService.createToken(authDTO.getUser(), accessTokenValidityInSeconds);
        authDTO.setAccessToken(accessToken);

        String newRefreshToken = securityService.createToken(authDTO.getUser(), refreshTokenValidityInSeconds);
        userService.updateUserToken(user.getId(), newRefreshToken);
        ResponseCookie springCookie = ResponseCookie
                .from("refreshToken", newRefreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenValidityInSeconds)
                .build();

        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Refresh token successfully", authDTO, null
        );
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, springCookie.toString())
                .body(res);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseObject> logout() throws Exception {
        String email = SecurityService.getCurrentUserLogin()
                .orElseThrow(() -> new UnauthorizedException("Invalid token"));

        UserEntity user = userService.getUserByEmail(email);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        userService.updateUserToken(user.getId(), null);

        ResponseCookie springCookie = ResponseCookie
                .from("refreshToken", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Logout successfully", null, null
        );
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, springCookie.toString())
                .body(res);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseObject> registerUser(@Valid @RequestBody CreateUserDTO createUserDTO) throws Exception {
        UserDTO userDTO = userService.registerUser(createUserDTO);
        ResponseObject res = new ResponseObject(HttpStatus.CREATED.value(),
                "User register successfully", userDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("activate")
    public ResponseEntity<ResponseObject> activateUser(@Valid @RequestBody ActivateUserDTO activateUserDTO) throws Exception {
        userService.activateUser(activateUserDTO);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "User activated successfully", null, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("reset-password")
    public ResponseEntity<ResponseObject> resetUserPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO) throws Exception {
        userService.resetPassword(resetPasswordDTO);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Reset password successfully", null, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("resend-mail")
    public ResponseEntity<ResponseObject> resendMail(@Valid @RequestBody ResendMailDTO resendMailDTO) throws Exception {
        userService.resendMail(resendMailDTO);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Resend mail successfully", null, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    public static AuthUserDTO convertDTO(UserEntity user) {
        AuthUserDTO authUserDTO = new AuthUserDTO();
        authUserDTO.setId(user.getId());
        authUserDTO.setEmail(user.getEmail());
        authUserDTO.setName(user.getName());
        authUserDTO.setRole(user.getRole().getName());
        List<Long> permissions = new ArrayList<>();
        if (user.getRole().getPermissions() != null && !user.getRole().getPermissions().isEmpty()) {
            user.getRole().getPermissions().forEach(permission -> permissions.add(permission.getId()));
        }
        authUserDTO.setPermissions(permissions);
        return authUserDTO;
    }
}
