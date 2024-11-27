package com.project.library.dto.borrow;

import jakarta.validation.constraints.*;
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
public class CreateBorrowDTO {
    @NotNull(message = "Borrow date is required")
    @FutureOrPresent(message = "Borrow date must be in the future or today")
    private Instant borrowDate;

    @NotNull(message = "Return date is required")
    @Future(message = "Return date must be in the future")
    private Instant returnDate;

    @NotNull(message = "User is required")
    private Long userId;

    @NotEmpty(message = "Borrow books are required")
    @Size(min = 1, message = "Borrow books must be greater than 1")
    private List<Long> books;
}
