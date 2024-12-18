package com.project.library.service.impl;

import com.project.library.dto.PageDTO;
import com.project.library.dto.role.*;
import com.project.library.entity.PermissionEntity;
import com.project.library.entity.RoleEntity;
import com.project.library.repository.PermissionRepo;
import com.project.library.repository.RoleRepo;
import com.project.library.service.RoleService;
import com.project.library.util.NotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private PermissionRepo permissionRepo;

    @Override
    public RoleDTO createRole(CreateRoleDTO createRoleDTO) throws Exception {
        if (roleRepo.existsByName(createRoleDTO.getName().trim())) {
            throw new BadRequestException("Role name already exists");
        }

        RoleEntity role = new RoleEntity();
        role.setName(createRoleDTO.getName().trim());
        role.setDescription(createRoleDTO.getDescription().trim());

        List<PermissionEntity> permissions = createRoleDTO.getPermissions().stream().map(permission ->
                permissionRepo.findById(permission).orElseThrow(() -> new NotFoundException("Permission not found with id: " + permission))).collect(Collectors.toList());
        role.setPermissions(permissions);
        return convertDTO(roleRepo.save(role));
    }

    @Override
    public RoleDTO updateRole(UpdateRoleDTO updateRoleDTO) throws Exception {
        RoleEntity role = roleRepo.findById(updateRoleDTO.getId())
                .orElseThrow(() -> new NotFoundException("Role not found"));

        role.setName(updateRoleDTO.getName().trim());
        role.setDescription(updateRoleDTO.getDescription().trim());

        List<PermissionEntity> permissions = updateRoleDTO.getPermissions().stream().map(permission ->
                permissionRepo.findById(permission).orElseThrow(() -> new NotFoundException("Permission not found with id: " + permission))).collect(Collectors.toList());
        role.setPermissions(permissions);
        return convertDTO(roleRepo.save(role));
    }

    @Override
    public PageDTO getRoles(Specification<RoleEntity> spec, Pageable pageable) throws Exception {
        Page<RoleEntity> pageData = roleRepo.findAll(spec, pageable);
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(pageable.getPageNumber() + 1);
        pageDTO.setSize(pageable.getPageSize());
        pageDTO.setTotalElements(pageData.getTotalElements());
        pageDTO.setTotalPages(pageData.getTotalPages());
        List<RoleDTO> roleDTOS = pageData.getContent()
                .stream()
                .map(RoleServiceImpl::convertDTO)
                .toList();
        pageDTO.setData(roleDTOS);
        return pageDTO;
    }

    @Override
    public RoleDTO getRoleById(Long id) throws Exception {
        RoleEntity role = roleRepo.findById(id).orElseThrow(() -> new NotFoundException("Role not found"));
        return convertDTO(role);
    }

    @Override
    public void deleteRole(Long id) throws Exception {
        RoleEntity role = roleRepo.findById(id).orElseThrow(() -> new NotFoundException("Role not found"));
        if (role.getId() == 1) {
            throw new BadRequestException("Cannot delete admin role");
        }
        roleRepo.delete(role);
    }

    public static RoleDTO convertDTO(RoleEntity role) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        roleDTO.setDescription(role.getDescription());
        roleDTO.setCreatedAt(role.getCreatedAt());
        roleDTO.setUpdatedAt(role.getUpdatedAt());
        List<Long> permissions = role.getPermissions().stream()
                .map(PermissionEntity::getId)
                .collect(Collectors.toList());
        roleDTO.setPermissions(permissions);
        roleDTO.setPermissions(permissions);
        return roleDTO;
    }
}
