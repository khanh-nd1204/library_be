package com.project.student.dto;

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
public class PageDTO {
    private int page;
    private int size;
    private Long totalElements;
    private int totalPages;
    private Object data;
}
