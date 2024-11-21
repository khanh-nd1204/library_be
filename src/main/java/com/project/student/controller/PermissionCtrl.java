package com.project.student.controller;

import com.project.student.dto.PageDTO;
import com.project.student.dto.ResponseObject;
import com.project.student.dto.permission.CreatePermissionDTO;
import com.project.student.dto.permission.PermissionDTO;
import com.project.student.dto.permission.UpdatePermissionDTO;
import com.project.student.entity.PermissionEntity;
import com.project.student.service.PermissionService;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/permissions")
public class PermissionCtrl {

    @Autowired
    private PermissionService permissionService;

    @PostMapping
    public ResponseEntity<ResponseObject> createPermission(@Valid @RequestBody CreatePermissionDTO createPermissionDTO) throws Exception {
        PermissionDTO permissionDTO = permissionService.createPermission(createPermissionDTO);
        ResponseObject res = new ResponseObject(HttpStatus.CREATED.value(),
                "Permission created successfully", permissionDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<ResponseObject> updatePermission(@Valid @RequestBody UpdatePermissionDTO updatePermissionDTO) throws Exception {
        PermissionDTO permissionDTO = permissionService.updatePermission(updatePermissionDTO);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Permission updated successfully", permissionDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ResponseObject> getAllPermissions(
            @Filter Specification<PermissionEntity> spec, Pageable pageable) throws Exception {
        PageDTO pageDTO = permissionService.getPermissions(spec, pageable);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Permissions fetched successfully", pageDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getPermissionById(@PathVariable Long id) throws Exception {
        PermissionDTO permissionDTO = permissionService.getPermissionById(id);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Permission fetched successfully", permissionDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deletePermissionById(@PathVariable Long id) throws Exception {
        permissionService.deletePermission(id);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Permission deleted successfully", null, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
