package com.project.student.dto.permission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class UpdatePermissionDTO {
    @NotNull(message = "ID is required")
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    @NotBlank(message = "API path is required")
    @Size(max = 100, message = "API path must be less than 100 characters")
    private String apiPath;

    @NotBlank(message = "Method is required")
    @Size(max = 100, message = "Method must be less than 100 characters")
    private String method;

    @NotBlank(message = "Module is required")
    @Size(max = 100, message = "Module must be less than 100 characters")
    private String module;
}
