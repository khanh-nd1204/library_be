package com.project.library.repository;

import com.project.library.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {
    UserEntity findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    UserEntity findByPhone(String phone);
    UserEntity findByEmailAndRefreshToken(String email, String refreshToken);
}
