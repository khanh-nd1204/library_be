package com.project.library.dto.book;

import jakarta.validation.constraints.*;
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
public class UpdateBookDTO {
    @NotNull(message = "ID is required")
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;

    @NotNull(message = "Publish year is required")
    @Min(value = 1924, message = "Publish year is invalid")
    @Max(value = 2024, message = "Publish year is invalid")
    private Integer publishYear;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than 0")
    private Integer quantity;

    @NotEmpty(message = "Authors are required")
    @Size(min = 1, message = "Authors must be greater than 1")
    private List<Long> authors;

    @NotEmpty(message = "Categories are required")
    @Size(min = 1, message = "Categories must be greater than 1")
    private List<Long> categories;

    @NotEmpty(message = "Book images are required")
    @Size(min = 1, message = "Book images must be greater than 1")
    private List<Long> images;

    @NotNull(message = "Publisher is required")
    private Long publisherId;
}
