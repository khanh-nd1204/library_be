package com.project.library.service.impl;

import com.project.library.dto.PageDTO;
import com.project.library.dto.permission.*;
import com.project.library.entity.PermissionEntity;
import com.project.library.repository.PermissionRepo;
import com.project.library.service.PermissionService;
import com.project.library.util.NotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepo permissionRepo;

    @Override
    public PermissionDTO createPermission(CreatePermissionDTO createPermissionDTO) throws Exception {
        if (permissionRepo.existsByApiPathAndMethodAndModule(createPermissionDTO.getApiPath().trim(),
                createPermissionDTO.getMethod().trim(), createPermissionDTO.getModule().trim())) {
            throw new BadRequestException("Permission already exist");
        }

        PermissionEntity permission = new PermissionEntity();
        permission.setName(createPermissionDTO.getName().trim());
        permission.setApiPath(createPermissionDTO.getApiPath().trim());
        permission.setModule(createPermissionDTO.getModule().trim());
        permission.setMethod(createPermissionDTO.getMethod().trim());
        return convertDTO(permissionRepo.save(permission));
    }

    @Override
    public PermissionDTO updatePermission(UpdatePermissionDTO updatePermissionDTO) throws Exception {
        PermissionEntity permission = permissionRepo.findById(updatePermissionDTO.getId())
                .orElseThrow(() -> new NotFoundException("Permission not found"));

        PermissionEntity permissionExist = permissionRepo.findByApiPathAndMethodAndModule(
                updatePermissionDTO.getApiPath(), updatePermissionDTO.getMethod(), updatePermissionDTO.getModule());
        if (permissionExist != null && !permissionExist.getId().equals(permission.getId())) {
            throw new BadRequestException("Permission already exist");
        }

        permission.setName(updatePermissionDTO.getName().trim());
        permission.setApiPath(updatePermissionDTO.getApiPath().trim());
        permission.setModule(updatePermissionDTO.getModule().trim());
        permission.setMethod(updatePermissionDTO.getMethod().trim());
        return convertDTO(permissionRepo.save(permission));
    }

    @Override
    @Transactional(readOnly = true)
    public PageDTO getPermissions(Specification<PermissionEntity> spec, Pageable pageable) throws Exception {
        Page<PermissionEntity> pageData = permissionRepo.findAll(spec, pageable);
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(pageable.getPageNumber() + 1);
        pageDTO.setSize(pageable.getPageSize());
        pageDTO.setTotalElements(pageData.getTotalElements());
        pageDTO.setTotalPages(pageData.getTotalPages());
        List<PermissionDTO> permissionDTOs = pageData.getContent()
                .stream()
                .map(PermissionServiceImpl::convertDTO)
                .toList();
        pageDTO.setData(permissionDTOs);
        return pageDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public PermissionDTO getPermissionById(Long id) throws Exception {
        PermissionEntity permission = permissionRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Permission not found"));
        return convertDTO(permission);
    }

    @Override
    public void deletePermission(Long id) throws Exception {
        PermissionEntity permission = permissionRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Permission not found"));
        if (permission.getRoles() != null && !permission.getRoles().isEmpty()) {
            permission.getRoles().forEach(role -> role.getPermissions().remove(permission));
        }
        permissionRepo.delete(permission);
    }

    public static PermissionDTO convertDTO(PermissionEntity permission) {
        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setId(permission.getId());
        permissionDTO.setName(permission.getName());
        permissionDTO.setApiPath(permission.getApiPath());
        permissionDTO.setModule(permission.getModule());
        permissionDTO.setMethod(permission.getMethod());
        permissionDTO.setCreatedAt(permission.getCreatedAt());
        permissionDTO.setUpdatedAt(permission.getUpdatedAt());
        return permissionDTO;
    }
}
