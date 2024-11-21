package com.project.student.service;

import com.project.student.dto.PageDTO;
import com.project.student.dto.permission.*;
import com.project.student.entity.PermissionEntity;
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
