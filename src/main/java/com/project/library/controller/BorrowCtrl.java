package com.project.library.controller;

import com.project.library.dto.PageDTO;
import com.project.library.dto.ResponseObject;
import com.project.library.dto.borrow.BorrowDTO;
import com.project.library.dto.borrow.CreateBorrowDTO;
import com.project.library.entity.BorrowEntity;
import com.project.library.service.BorrowService;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/borrows")
public class BorrowCtrl {

    @Autowired
    private BorrowService borrowService;

    @PostMapping
    public ResponseEntity<ResponseObject> createBorrow(@Valid @RequestBody CreateBorrowDTO createBorrowDTO) throws Exception {
        BorrowDTO borrowDTO = borrowService.createBorrow(createBorrowDTO);
        ResponseObject res = new ResponseObject(HttpStatus.CREATED.value(),
                "Borrow created successfully", borrowDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseObject> returnBorrow(@PathVariable Long id) throws Exception {
        BorrowDTO borrowDTO = borrowService.returnBorrow(id);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Borrow updated successfully", borrowDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ResponseObject> getBorrows(
            @Filter Specification<BorrowEntity> spec, Pageable pageable) throws Exception {
        PageDTO pageDTO = borrowService.getBorrows(spec, pageable);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Borrows fetched successfully", pageDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getBorrow(@PathVariable Long id) throws Exception {
        BorrowDTO borrowDTO = borrowService.getBorrow(id);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Borrow fetched successfully", borrowDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteBorrow(@PathVariable Long id) throws Exception {
        borrowService.deleteBorrow(id);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Borrow deleted successfully", null, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/by-user")
    public ResponseEntity<ResponseObject> getBorrowsByUser(Pageable pageable) throws Exception {
        PageDTO pageDTO = borrowService.getBorrowsByUser(pageable);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Borrows fetched successfully", pageDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
