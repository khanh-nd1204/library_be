package com.project.library.service.impl;

import com.project.library.dto.PageDTO;
import com.project.library.dto.borrow.*;
import com.project.library.entity.*;
import com.project.library.repository.BookRepo;
import com.project.library.repository.BorrowRepo;
import com.project.library.repository.UserRepo;
import com.project.library.service.BorrowService;
import com.project.library.service.SecurityService;
import com.project.library.util.NotFoundException;
import com.project.library.util.UnauthorizedException;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BorrowServiceImpl implements BorrowService {

    @Autowired
    private BorrowRepo borrowRepo;

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FilterParser filterParser;

    @Autowired
    private FilterSpecificationConverter filterSpecificationConverter;

    @Override
    public BorrowDTO createBorrow(CreateBorrowDTO createBorrowDTO) throws Exception {
        BorrowEntity borrow = new BorrowEntity();
        borrow.setBorrowDate(createBorrowDTO.getBorrowDate());
        borrow.setReturnDate(createBorrowDTO.getReturnDate());
        borrow.setStatus(1);

        UserEntity user = userRepo.findById(createBorrowDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        borrow.setUser(user);

        List<BookEntity> books = createBorrowDTO.getBooks().stream()
                .map(bookId -> bookRepo.findById(bookId)
                        .orElseThrow(() -> new NotFoundException("Book not found with id: " + bookId)))
                .peek(book -> {
                    if (book.getQuantity() <= 0) {
                        throw new NotFoundException("Book with id " + book.getId() + " is out of stock");
                    }
                    book.setQuantity(book.getQuantity() - 1);
                }).collect(Collectors.toList());
        bookRepo.saveAll(books);
        borrow.setBooks(books);

        return convertDTO(borrowRepo.save(borrow));
    }

    @Override
    public BorrowDTO returnBorrow(Long id) throws Exception {
        BorrowEntity borrow = borrowRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Borrow not found"));
        if (!borrow.getStatus().equals("Borrowed")) {
            throw new BadRequestException("Book is already returned");
        }
        borrow.setStatus(2);
        borrow.getBooks().forEach(book -> book.setQuantity(book.getQuantity() + 1));
        bookRepo.saveAll(borrow.getBooks());
        return convertDTO(borrowRepo.save(borrow));
    }

    @Override
    @Transactional(readOnly = true)
    public PageDTO getBorrows(Specification<BorrowEntity> spec, Pageable pageable) throws Exception {
        Page<BorrowEntity> pageData = borrowRepo.findAll(spec, pageable);
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(pageable.getPageNumber() + 1);
        pageDTO.setSize(pageable.getPageSize());
        pageDTO.setTotalElements(pageData.getTotalElements());
        pageDTO.setTotalPages(pageData.getTotalPages());
        List<BorrowDTO> borrowDTOS = pageData.getContent()
                .stream()
                .map(BorrowServiceImpl::convertDTO)
                .toList();
        pageDTO.setData(borrowDTOS);
        return pageDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public PageDTO getBorrowsByUser(Pageable pageable) throws Exception {
        String email = SecurityService.getCurrentUserLogin()
                .orElseThrow(() -> new UnauthorizedException("No user is logged in"));
        FilterNode filterNode = filterParser.parse("user.email:'" + email + "'");
        FilterSpecification<BorrowEntity> spec = filterSpecificationConverter.convert(filterNode);
        Page<BorrowEntity> pageData = borrowRepo.findAll(spec, pageable);
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(pageable.getPageNumber() + 1);
        pageDTO.setSize(pageable.getPageSize());
        pageDTO.setTotalElements(pageData.getTotalElements());
        pageDTO.setTotalPages(pageData.getTotalPages());
        List<BorrowDTO> borrowDTOS = pageData.getContent()
                .stream()
                .map(BorrowServiceImpl::convertDTO)
                .toList();
        pageDTO.setData(borrowDTOS);
        return pageDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public BorrowDTO getBorrow(Long id) throws Exception {
        BorrowEntity borrow = borrowRepo.findById(id).orElseThrow(() -> new NotFoundException("Borrow not found"));
        return convertDTO(borrow);
    }

    @Override
    public void deleteBorrow(Long id) throws Exception {
        if (!borrowRepo.existsById(id)) {
            throw new NotFoundException("Borrow not found");
        }
        borrowRepo.deleteById(id);
    }

    public static BorrowDTO convertDTO(BorrowEntity borrow) {
        BorrowDTO borrowDTO = new BorrowDTO();
        borrowDTO.setId(borrow.getId());
        borrowDTO.setBorrowDate(borrow.getBorrowDate());
        borrowDTO.setReturnDate(borrow.getReturnDate());
        borrowDTO.setStatus(borrow.getStatus());
        borrowDTO.setCreatedAt(borrow.getCreatedAt());
        borrowDTO.setUpdatedAt(borrow.getUpdatedAt());

        BorrowUserDTO user = new BorrowUserDTO();
        user.setId(borrow.getUser().getId());
        user.setName(borrow.getUser().getName());
        user.setEmail(borrow.getUser().getEmail());
        user.setAddress(borrow.getUser().getAddress());
        user.setPhone(borrow.getUser().getPhone());
        borrowDTO.setUser(user);

        List<BorrowBookDTO> books = new ArrayList<>();
        if (borrow.getBooks() != null && !borrow.getBooks().isEmpty()) {
            for (BookEntity book : borrow.getBooks()) {
                BorrowBookDTO bookDTO = new BorrowBookDTO();
                bookDTO.setId(book.getId());
                bookDTO.setName(book.getName());
                bookDTO.setImage(book.getImages().stream().map(ImageEntity::getImageUrl).collect(Collectors.toList()));
                bookDTO.setAuthors(book.getAuthors().stream().map(AuthorEntity::getName).collect(Collectors.toList()));
                bookDTO.setCategories(book.getCategories().stream().map(CategoryEntity::getName).collect(Collectors.toList()));
                books.add(bookDTO);
            }
        }
        borrowDTO.setBooks(books);

        return borrowDTO;
    }
}
