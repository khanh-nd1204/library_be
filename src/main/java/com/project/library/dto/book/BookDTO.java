package com.project.library.dto.book;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class BookDTO {
    private Long id;
    private String name;
    private String description;
    private Integer publishYear;
    private Integer quantity;
    private boolean active;
    private List<String> authors;
    private String publisher;
    private List<String> categories;
    private List<String> images;
    @JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy", timezone = "GMT+7")
    private Instant createdAt;
    @JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy", timezone = "GMT+7")
    private Instant updatedAt;
}