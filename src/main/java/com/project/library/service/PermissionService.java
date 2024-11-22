package com.project.library.service;

import com.project.library.dto.PageDTO;
import com.project.library.dto.permission.*;
import com.project.library.entity.PermissionEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public interface PermissionService {
    PermissionDTO createPermission(CreatePermissionDTO createPermissionDTO) throws Exception;
    PermissionDTO updatePermission(UpdatePermissionDTO updatePermissionDTO) throws Exception;
    PageDTO getPermissions(Specification<PermissionEntity> spec, Pageable pageable) throws Exception;
    PermissionDTO getPermissionById(Long id) throws Exception;
    void deletePermission(Long id) throws Exception;
}
