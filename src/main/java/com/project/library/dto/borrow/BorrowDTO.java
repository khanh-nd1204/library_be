package com.project.library.dto.borrow;

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
public class BorrowDTO {
    private Long id;
    @JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy", timezone = "GMT+7")
    private Instant borrowDate;
    @JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy", timezone = "GMT+7")
    private Instant returnDate;
    private Integer status;
    private BorrowUserDTO user;
    private List<BorrowBookDTO> books;
    @JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy", timezone = "GMT+7")
    private Instant createdAt;
    @JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy", timezone = "GMT+7")
    private Instant updatedAt;
}
