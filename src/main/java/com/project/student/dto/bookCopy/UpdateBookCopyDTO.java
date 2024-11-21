package com.project.student.dto.bookCopy;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class UpdateBookCopyDTO {
    @NotNull(message = "ID is required")
    private Long id;

    @NotBlank(message = "Status is required")
    private String status;
}
