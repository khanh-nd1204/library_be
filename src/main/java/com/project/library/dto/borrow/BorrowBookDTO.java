package com.project.library.dto.borrow;

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
public class BorrowBookDTO {
    private Long id;
    private String name;
    private Integer publishYear;
    private List<String> image;
    private List<String> authors;
    private List<String> categories;
}
