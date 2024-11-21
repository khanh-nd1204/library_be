package com.project.student.service;

import com.project.student.dto.PageDTO;
import com.project.student.dto.book.*;
import com.project.student.entity.BookEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public interface BookService {
    BookDTO createBook(CreateBookDTO createBookDTO) throws Exception;
    BookDTO updateBook(UpdateBookDTO updateBookDTO) throws Exception;
    BookDTO getBook(Long id) throws Exception;
    PageDTO getBooks(Specification<BookEntity> spec, Pageable pageable) throws Exception;
    void deleteBook(Long id) throws Exception;
}
