package com.project.student.service.impl;

import com.project.student.dto.PageDTO;
import com.project.student.dto.bookCopy.BookCopyDTO;
import com.project.student.dto.bookCopy.UpdateBookCopyDTO;
import com.project.student.entity.BookCopyEntity;
import com.project.student.entity.BookEntity;
import com.project.student.repository.BookCopyRepo;
import com.project.student.repository.BookRepo;
import com.project.student.service.BookCopyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookCopyServiceImpl implements BookCopyService {

    @Autowired
    private BookCopyRepo bookCopyRepo;
    @Autowired
    private BookRepo bookRepo;

    @Override
    @Transactional(readOnly = true)
    public BookCopyDTO getBookCopy(Long id) throws Exception {
        BookCopyEntity bookCopy = bookCopyRepo.findById(id).orElseThrow(() -> new Exception("Book not found"));
        return convertDTO(bookCopy);
    }

    @Override
    @Transactional(readOnly = true)
    public PageDTO getBookCopies(Specification<BookCopyEntity> spec, Pageable pageable) throws Exception {
        Page<BookCopyEntity> pageData = bookCopyRepo.findAll(spec, pageable);
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(pageable.getPageNumber() + 1);
        pageDTO.setSize(pageable.getPageSize());
        pageDTO.setTotalElements(pageData.getTotalElements());
        pageDTO.setTotalPages(pageData.getTotalPages());
        List<BookCopyDTO> bookCopyDTOS = pageData.getContent()
                .stream()
                .map(BookCopyServiceImpl::convertDTO)
                .toList();
        pageDTO.setData(bookCopyDTOS);
        return pageDTO;
    }

    @Override
    public BookCopyDTO updateBookCopy(UpdateBookCopyDTO updateBookCopyDTO) throws Exception {
        BookCopyEntity bookCopy = bookCopyRepo.findById(updateBookCopyDTO.getId())
                .orElseThrow(() -> new Exception("Book not found"));
        bookCopy.setStatus(updateBookCopyDTO.getStatus());
        return convertDTO(bookCopyRepo.save(bookCopy));
    }

    @Override
    public void deleteBookCopy(Long id) throws Exception {
        BookCopyEntity bookCopy = bookCopyRepo.findById(id).orElseThrow(() -> new Exception("Book not found"));
        BookEntity book = bookCopy.getBook();
        if (book.getBookCopies() != null && !book.getBookCopies().isEmpty()) {
            book.getBookCopies().remove(bookCopy);
            book.setQuantity(book.getQuantity() - 1);
            bookRepo.save(book);
        }
        bookCopyRepo.delete(bookCopy);
    }

    public static BookCopyDTO convertDTO(BookCopyEntity bookCopy) {
        BookCopyDTO bookCopyDTO = new BookCopyDTO();
        bookCopyDTO.setId(bookCopy.getId());
        bookCopyDTO.setStatus(bookCopy.getStatus());
        bookCopyDTO.setCreatedAt(bookCopy.getCreatedAt());
        bookCopyDTO.setUpdatedAt(bookCopy.getUpdatedAt());
        bookCopyDTO.setBookId(bookCopy.getBook().getId());
        return bookCopyDTO;
    }
}
