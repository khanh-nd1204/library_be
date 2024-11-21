package com.project.student.service;

import com.project.student.dto.PageDTO;
import com.project.student.dto.bookCopy.*;
import com.project.student.entity.BookCopyEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface BookCopyService {
    BookCopyDTO getBookCopy(Long id) throws Exception;
    PageDTO getBookCopies(Specification<BookCopyEntity> spec, Pageable pageable) throws Exception;
    BookCopyDTO updateBookCopy(UpdateBookCopyDTO updateBookCopyDTO) throws Exception;
    void deleteBookCopy(Long id) throws Exception;
}
