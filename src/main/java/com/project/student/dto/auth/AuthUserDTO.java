package com.project.student.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class AuthUserDTO {
    private Long id;
    private String name;
    private String email;
    private String role;
    private List<Long> permissions;
}
