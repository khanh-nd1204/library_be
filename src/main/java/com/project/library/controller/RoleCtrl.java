package com.project.library.controller;

import com.project.library.dto.PageDTO;
import com.project.library.dto.ResponseObject;
import com.project.library.dto.role.CreateRoleDTO;
import com.project.library.dto.role.RoleDTO;
import com.project.library.dto.role.UpdateRoleDTO;
import com.project.library.entity.RoleEntity;
import com.project.library.service.RoleService;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/roles")
public class RoleCtrl {

    @Autowired
    private RoleService roleService;

    @PostMapping
    public ResponseEntity<ResponseObject> createRole(@Valid @RequestBody CreateRoleDTO createRoleDTO) throws Exception {
        RoleDTO roleDTO = roleService.createRole(createRoleDTO);
        ResponseObject res = new ResponseObject(HttpStatus.CREATED.value(),
                "Role created successfully", roleDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<ResponseObject> updateRole(@Valid @RequestBody UpdateRoleDTO updateRoleDTO) throws Exception {
        RoleDTO roleDTO = roleService.updateRole(updateRoleDTO);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Role updated successfully", roleDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ResponseObject> getRoles(
            @Filter Specification<RoleEntity> spec, Pageable pageable) throws Exception {
        PageDTO pageDTO = roleService.getRoles(spec, pageable);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Roles fetched successfully", pageDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getRole(@PathVariable Long id) throws Exception {
        RoleDTO roleDTO = roleService.getRoleById(id);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Role fetched successfully", roleDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteRole(@PathVariable Long id) throws Exception {
        roleService.deleteRole(id);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Role deleted successfully", null, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
