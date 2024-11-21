package com.project.student.service.impl;

import com.project.student.dto.PageDTO;
import com.project.student.dto.mail.MailDTO;
import com.project.student.dto.mail.ResendMailDTO;
import com.project.student.dto.user.*;
import com.project.student.entity.RoleEntity;
import com.project.student.entity.UserEntity;
import com.project.student.repository.RoleRepo;
import com.project.student.repository.UserRepo;
import com.project.student.service.MailService;
import com.project.student.service.UserService;
import com.project.student.util.NotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private MailService mailService;

    @Value("${spring.mail.expiry-in-seconds}")
    private int expiryInSeconds;

    @Override
    public UserDTO createUser(CreateUserDTO createUserDTO) throws Exception {
        if (userRepo.existsByEmail(createUserDTO.getEmail().trim())) {
            throw new BadRequestException("Email already exists");
        }

        if (userRepo.existsByPhone(createUserDTO.getPhone().trim())) {
            throw new BadRequestException("Phone already exists");
        }

        UserEntity user = new UserEntity();
        user.setName(createUserDTO.getName().trim());
        user.setEmail(createUserDTO.getEmail().trim());
        user.setPassword(passwordEncoder.encode(createUserDTO.getPassword().trim()));
        user.setAddress(createUserDTO.getAddress().trim());
        user.setPhone(createUserDTO.getPhone().trim());
        user.setActive(true);

        RoleEntity role = roleRepo.findById(createUserDTO.getRoleId())
                .orElseThrow(() -> new NotFoundException("Role not found"));
        user.setRole(role);

        return convertDTO(userRepo.save(user));
    }

    @Override
    public UserDTO updateUser(UpdateUserDTO updateUserDTO) throws Exception {
        UserEntity user = userRepo.findById(updateUserDTO.getId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        UserEntity userByPhone = userRepo.findByPhone(updateUserDTO.getPhone().trim());
        if (userByPhone != null && !user.getId().equals(userByPhone.getId())) {
            throw new BadRequestException("Phone already exists");
        }

        user.setName(updateUserDTO.getName().trim());
        user.setAddress(updateUserDTO.getAddress().trim());
        user.setPhone(updateUserDTO.getPhone().trim());
        return convertDTO(userRepo.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) throws Exception {
        UserEntity user = userRepo.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        return convertDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public PageDTO getUsers(Specification<UserEntity> spec, Pageable pageable) throws Exception {
        Page<UserEntity> pageData = userRepo.findAll(spec, pageable);
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(pageable.getPageNumber() + 1);
        pageDTO.setSize(pageable.getPageSize());
        pageDTO.setTotalElements(pageData.getTotalElements());
        pageDTO.setTotalPages(pageData.getTotalPages());
        List<UserDTO> userDTOs = pageData.getContent()
                .stream()
                .map(UserServiceImpl::convertDTO)
                .toList();
        pageDTO.setData(userDTOs);
        return pageDTO;
    }

    @Override
    public void deleteUser(Long id) throws Exception {
        UserEntity user = userRepo.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        if (user.getRole().getId() == 1) {
            throw new BadRequestException("Cannot delete admin");
        }
        user.setActive(false);
        userRepo.save(user);
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public void updateUserToken(Long id, String token) throws Exception {
        UserEntity user = userRepo.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        user.setRefreshToken(token);
        userRepo.save(user);
    }

    @Override
    public UserEntity getUserByEmailAndRefreshToken(String email, String refreshToken) throws Exception {
        return userRepo.findByEmailAndRefreshToken(email, refreshToken);
    }

    @Override
    public UserDTO registerUser(CreateUserDTO createUserDTO) throws Exception {
        if (userRepo.existsByEmail(createUserDTO.getEmail().trim())) {
            throw new BadRequestException("Email already exists");
        }

        if (userRepo.existsByPhone(createUserDTO.getPhone().trim())) {
            throw new BadRequestException("Phone already exists");
        }

        UserEntity user = new UserEntity();
        user.setName(createUserDTO.getName().trim());
        user.setEmail(createUserDTO.getEmail().trim());
        user.setPassword(passwordEncoder.encode(createUserDTO.getPassword().trim()));
        user.setAddress(createUserDTO.getAddress().trim());
        user.setPhone(createUserDTO.getPhone().trim());
        user.setActive(false);

        // default user
        RoleEntity role = roleRepo.findById(2L).orElseThrow(() -> new NotFoundException("Role not found"));
        user.setRole(role);

        int otp = (int) Math.floor(100000 + Math.random() * 900000);
        Instant otpExpried = Instant.now().plusSeconds(expiryInSeconds);
        user.setOtp(otp);
        user.setOtpExpired(otpExpried);

        MailDTO mailDTO = new MailDTO();
        mailDTO.setTo(createUserDTO.getEmail());
        mailDTO.setName(createUserDTO.getName().trim());
        mailDTO.setSubject("Activate account");
        mailDTO.setOtp(otp);
        mailDTO.setTemplate("activate-account");
        mailService.sendEmailFromTemplateSync(mailDTO);
        return convertDTO(userRepo.save(user));
    }

    @Override
    public void activateUser(ActivateUserDTO activateUserDTO) throws Exception {
        UserEntity user = userRepo.findByEmail(activateUserDTO.getEmail());
        if (user == null) {
            throw new NotFoundException("Email is not exist");
        }

        if (user.isActive()) {
            throw new BadRequestException("Account has already been activated");
        }

        if (user.getOtp() == null) {
            throw new NotFoundException("OTP not found for the provided email");
        }

        if (user.getOtp() != activateUserDTO.getOtp()) {
            throw new BadRequestException("Invalid OTP");
        }

        if (user.getOtpExpired().isBefore(Instant.now())) {
            throw new BadRequestException("OTP has expired");
        }

        user.setActive(true);
        user.setOtp(null);
        user.setOtpExpired(null);
        userRepo.save(user);
    }

    @Override
    public void resetPassword(ResetPasswordDTO resetPasswordDTO) throws Exception {
        UserEntity user = userRepo.findByEmail(resetPasswordDTO.getEmail());
        if (user == null) {
            throw new NotFoundException("Email is not exist");
        }

        if (!user.isActive()) {
            throw new BadRequestException("Account has not been activated");
        }

        if (user.getOtp() == null) {
            throw new NotFoundException("OTP not found for the provided email");
        }

        if (user.getOtp() != resetPasswordDTO.getOtp()) {
            throw new BadRequestException("Invalid OTP");
        }

        if (user.getOtpExpired().isBefore(Instant.now())) {
            throw new BadRequestException("OTP has expired");
        }

        user.setPassword(passwordEncoder.encode(resetPasswordDTO.getPassword().trim()));
        user.setOtp(null);
        user.setOtpExpired(null);
        userRepo.save(user);
    }

    @Override
    public void resendMail(ResendMailDTO resendMailDTO) throws Exception {
        UserEntity user = userRepo.findByEmail(resendMailDTO.getEmail());
        if (user == null) {
            throw new NotFoundException("Email is not exist");
        }

        if (user.isActive() && resendMailDTO.getType().equals("activate")) {
            throw new BadRequestException("Account has already been activated");
        }

        if (!user.isActive() && resendMailDTO.getType().equals("reset-password")) {
            throw new BadRequestException("Account has not been activated");
        }

        int otp = (int) Math.floor(100000 + Math.random() * 900000);
        Instant otpExpried = Instant.now().plusSeconds(expiryInSeconds);
        user.setOtp(otp);
        user.setOtpExpired(otpExpried);
        userRepo.save(user);

        MailDTO mailDTO = new MailDTO();
        mailDTO.setTo(user.getEmail());
        mailDTO.setName(user.getName().trim());
        if (resendMailDTO.getType().equals("activate")) {
            mailDTO.setSubject("Activate account");
            mailDTO.setTemplate("activate-account");
        }
        if (resendMailDTO.getType().equals("reset-password")) {
            mailDTO.setSubject("Reset password");
            mailDTO.setTemplate("reset-password");
        }
        mailDTO.setOtp(otp);
        mailService.sendEmailFromTemplateSync(mailDTO);
    }

    @Override
    public void changePassword(ChangePasswordDTO changePasswordDTO) throws Exception {
        UserEntity user = userRepo.findById(changePasswordDTO.getId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }

        String newPassword = passwordEncoder.encode(changePasswordDTO.getNewPassword());
        user.setPassword(newPassword);
        userRepo.save(user);
    }

    public static UserDTO convertDTO(UserEntity user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        userDTO.setAddress(user.getAddress());
        userDTO.setActive(user.isActive());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        userDTO.setRole(user.getRole().getName());
        return userDTO;
    }
}
