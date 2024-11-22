package com.project.library.repository;

import com.project.library.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PermissionRepo extends JpaRepository<PermissionEntity, Long>, JpaSpecificationExecutor<PermissionEntity> {
    boolean existsByApiPathAndMethodAndModule(String apiPath, String method, String module);
    PermissionEntity findByApiPathAndMethodAndModule(String apiPath, String method, String module);
}
