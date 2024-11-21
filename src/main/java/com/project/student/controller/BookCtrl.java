package com.project.student.controller;

import com.project.student.dto.PageDTO;
import com.project.student.dto.ResponseObject;
import com.project.student.dto.book.BookDTO;
import com.project.student.dto.book.CreateBookDTO;
import com.project.student.dto.book.UpdateBookDTO;
import com.project.student.entity.BookEntity;
import com.project.student.entity.UserEntity;
import com.project.student.service.BookService;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/books")
public class BookCtrl {

    @Autowired
    private BookService bookService;

    @PostMapping
    public ResponseEntity<ResponseObject> createBook(@Valid @RequestBody CreateBookDTO createBookDTO) throws Exception {
        BookDTO bookDTO = bookService.createBook(createBookDTO);
        ResponseObject res = new ResponseObject(HttpStatus.CREATED.value(),
                "Book created successfully", bookDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<ResponseObject> updateBook(@Valid @RequestBody UpdateBookDTO updateBookDTO) throws Exception {
        BookDTO bookDTO = bookService.updateBook(updateBookDTO);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Book updated successfully", bookDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ResponseObject> getBooks(
            @Filter Specification<BookEntity> spec, Pageable pageable) throws Exception {
        PageDTO pageDTO = bookService.getBooks(spec, pageable);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Books fetched successfully", pageDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getBook(@PathVariable Long id) throws Exception {
        BookDTO bookDTO = bookService.getBook(id);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Book fetched successfully", bookDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteBook(@PathVariable Long id) throws Exception {
        bookService.deleteBook(id);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Book deleted successfully", null, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
