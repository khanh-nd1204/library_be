package com.project.library.dto.borrow;

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
public class BorrowUserDTO {
    private Long id;
    private String name;
    private String email;
    private String address;
    private String phone;
}
