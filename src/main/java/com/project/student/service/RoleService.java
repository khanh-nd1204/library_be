package com.project.student.service;

import com.project.student.dto.PageDTO;
import com.project.student.dto.role.*;
import com.project.student.entity.RoleEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public interface RoleService {
    RoleDTO createRole(CreateRoleDTO createRoleDTO) throws Exception;
    RoleDTO updateRole(UpdateRoleDTO updateRoleDTO) throws Exception;
    PageDTO getRoles(Specification<RoleEntity> spec, Pageable pageable) throws Exception;
    RoleDTO getRoleById(Long id) throws Exception;
    void deleteRole(Long id) throws Exception;
}
