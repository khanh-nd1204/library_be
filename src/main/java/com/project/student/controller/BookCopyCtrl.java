package com.project.student.controller;

import com.project.student.dto.PageDTO;
import com.project.student.dto.ResponseObject;
import com.project.student.dto.bookCopy.BookCopyDTO;
import com.project.student.dto.bookCopy.UpdateBookCopyDTO;
import com.project.student.entity.BookCopyEntity;
import com.project.student.service.BookCopyService;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/book-copy")
public class BookCopyCtrl {

    @Autowired
    private BookCopyService bookCopyService;

    @PatchMapping
    public ResponseEntity<ResponseObject> updateBookCopy(@Valid @RequestBody UpdateBookCopyDTO updateBookCopyDTO) throws Exception {
        BookCopyDTO bookCopyDTO = bookCopyService.updateBookCopy(updateBookCopyDTO);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Book updated successfully", bookCopyDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ResponseObject> getBookCopies(
            @Filter Specification<BookCopyEntity> spec, Pageable pageable) throws Exception {
        PageDTO pageDTO = bookCopyService.getBookCopies(spec, pageable);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Books fetched successfully", pageDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getBookCopy(@PathVariable Long id) throws Exception {
        BookCopyDTO bookCopyDTO = bookCopyService.getBookCopy(id);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Book fetched successfully", bookCopyDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteBookCopy(@PathVariable Long id) throws Exception {
        bookCopyService.deleteBookCopy(id);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Book deleted successfully", null, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
