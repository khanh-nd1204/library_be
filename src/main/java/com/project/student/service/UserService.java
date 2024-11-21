package com.project.student.service;

import com.project.student.dto.PageDTO;
import com.project.student.dto.mail.ResendMailDTO;
import com.project.student.dto.user.*;
import com.project.student.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserDTO createUser(CreateUserDTO createUserDTO) throws Exception;
    UserDTO updateUser(UpdateUserDTO updateUserDTO) throws Exception;
    UserDTO getUserById(Long id) throws Exception;
    PageDTO getUsers(Specification<UserEntity> spec, Pageable pageable) throws Exception;
    void deleteUser(Long id) throws Exception;
    UserEntity getUserByEmail(String email);
    void updateUserToken(Long id, String token) throws Exception;
    UserEntity getUserByEmailAndRefreshToken(String email, String refreshToken) throws Exception;
    UserDTO registerUser(CreateUserDTO createUserDTO) throws Exception;
    void activateUser(ActivateUserDTO activateUserDTO) throws Exception;
    void resetPassword(ResetPasswordDTO resetUserPassDTO) throws Exception;
    void resendMail(ResendMailDTO resendMailDTO) throws Exception;
    void changePassword(ChangePasswordDTO changePasswordDTO) throws Exception;
}
