package com.project.library.dto.user;

import jakarta.validation.constraints.*;
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
public class ActivateUserDTO {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    private String email;

    @NotNull(message = "OTP is required")
    @Min(value = 100000, message = "OTP must be a 6-digit number")
    @Max(value = 999999, message = "OTP must be a 6-digit number")
    private int otp;
}
