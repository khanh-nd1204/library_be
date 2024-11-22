package com.project.library.service;

import com.project.library.dto.PageDTO;
import com.project.library.dto.book.*;
import com.project.library.entity.BookEntity;
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
