package com.project.library.controller;


import com.project.library.dto.PageDTO;
import com.project.library.dto.ResponseObject;
import com.project.library.dto.user.ChangePasswordDTO;
import com.project.library.dto.user.CreateUserDTO;
import com.project.library.dto.user.UpdateUserDTO;
import com.project.library.dto.user.UserDTO;
import com.project.library.entity.UserEntity;
import com.project.library.service.UserService;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
public class UserCtrl {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<ResponseObject> createUser(@Valid @RequestBody CreateUserDTO createUserDTO) throws Exception {
        UserDTO userDTO = userService.createUser(createUserDTO);
        ResponseObject res = new ResponseObject(HttpStatus.CREATED.value(),
                "User created successfully", userDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<ResponseObject> updateUser(@Valid @RequestBody UpdateUserDTO updateUserDTO) throws Exception {
        UserDTO userDTO = userService.updateUser(updateUserDTO);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "User updated successfully", userDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ResponseObject> getUsers(
            @Filter Specification<UserEntity> spec, Pageable pageable) throws Exception {
        PageDTO pageDTO = userService.getUsers(spec, pageable);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Users fetched successfully", pageDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getUser(@PathVariable Long id) throws Exception {
        UserDTO userDTO = userService.getUserById(id);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "User fetched successfully", userDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteUser(@PathVariable Long id) throws Exception {
        userService.deleteUser(id);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "User deleted successfully", null, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/change-password")
    public ResponseEntity<ResponseObject> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) throws Exception {
        userService.changePassword(changePasswordDTO);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Password changed successfully", null, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}
