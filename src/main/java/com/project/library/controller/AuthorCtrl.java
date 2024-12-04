package com.project.library.controller;

import com.project.library.dto.PageDTO;
import com.project.library.dto.ResponseObject;
import com.project.library.dto.author.AuthorDTO;
import com.project.library.dto.author.CreateAuthorDTO;
import com.project.library.dto.author.UpdateAuthorDTO;
import com.project.library.entity.AuthorEntity;
import com.project.library.service.AuthorService;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/authors")
public class AuthorCtrl {

    @Autowired
    private AuthorService authorService;

    @PostMapping
    public ResponseEntity<ResponseObject> createAuthor(@Valid @RequestBody CreateAuthorDTO createAuthorDTO) throws Exception {
        AuthorDTO authorDTO = authorService.createAuthor(createAuthorDTO);
        ResponseObject res = new ResponseObject(HttpStatus.CREATED.value(),
                "Author created successfully", authorDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<ResponseObject> updateAuthor(@Valid @RequestBody UpdateAuthorDTO updateAuthorDTO) throws Exception {
        AuthorDTO authorDTO = authorService.updateAuthor(updateAuthorDTO);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Author updated successfully", authorDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ResponseObject> getAuthors(
            @Filter Specification<AuthorEntity> spec, Pageable pageable) throws Exception {
        PageDTO pageDTO = authorService.getAuthors(spec, pageable);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Authors fetched successfully", pageDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getAuthor(@PathVariable("id") Long id) throws Exception {
        AuthorDTO authorDTO = authorService.getAuthor(id);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Author fetched successfully", authorDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteAuthor(@PathVariable("id") Long id) throws Exception {
        authorService.deleteAuthor(id);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Author deleted successfully", null, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
