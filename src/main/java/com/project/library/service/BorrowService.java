package com.project.library.service;

import com.project.library.dto.PageDTO;
import com.project.library.dto.borrow.*;
import com.project.library.entity.BorrowEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public interface BorrowService {
    BorrowDTO createBorrow(CreateBorrowDTO createBorrowDTO) throws Exception;
    BorrowDTO returnBorrow(Long id) throws Exception;
    PageDTO getBorrows(Specification<BorrowEntity> spec, Pageable pageable) throws Exception;
    PageDTO getBorrowsByUser(Pageable pageable) throws Exception;
    BorrowDTO getBorrow(Long id) throws Exception;
    void deleteBorrow(Long id) throws Exception;
}
