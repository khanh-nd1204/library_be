package com.project.student.service.impl;

import com.project.student.dto.PageDTO;
import com.project.student.dto.book.BookDTO;
import com.project.student.dto.book.CreateBookDTO;
import com.project.student.dto.book.UpdateBookDTO;
import com.project.student.entity.*;
import com.project.student.repository.*;
import com.project.student.service.BookService;
import com.project.student.util.NotFoundException;
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
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private AuthorRepo authorRepo;

    @Autowired
    private PublisherRepo publisherRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private BookCopyRepo bookCopyRepo;

    @Override
    public BookDTO createBook(CreateBookDTO createBookDTO) throws Exception {
        BookEntity book = new BookEntity();
        book.setName(createBookDTO.getName());
        book.setDescription(createBookDTO.getDescription());
        book.setPublishYear(createBookDTO.getPublishYear());
        book.setQuantity(createBookDTO.getQuantity());
        book.setActive(true);

        List<AuthorEntity> authors = createBookDTO.getAuthors().stream().map(author ->
                authorRepo.findById(author).orElseThrow(() -> new NotFoundException("Author not found with id: " + author))).collect(Collectors.toList());
        book.setAuthors(authors);

        List<CategoryEntity> categories = createBookDTO.getCategories().stream().map(category ->
                categoryRepo.findById(category).orElseThrow(() -> new NotFoundException("Category not found with id: " + category))).collect(Collectors.toList());
        book.setCategories(categories);

        PublisherEntity publisher = publisherRepo.findById(createBookDTO.getPublisherId())
                .orElseThrow(() -> new NotFoundException("Publisher not found"));
        book.setPublisher(publisher);

        List<BookCopyEntity> bookCopies = new ArrayList<>();
        for (int i = 0; i < createBookDTO.getQuantity(); i++) {
            BookCopyEntity bookCopy = new BookCopyEntity();
            bookCopy.setStatus("Available");
            bookCopy.setBook(book);
            bookCopies.add(bookCopy);
        }
        book.setBookCopies(bookCopies);
        bookCopyRepo.saveAll(bookCopies);

        return convertDTO(bookRepo.save(book));
    }

    @Override
    public BookDTO updateBook(UpdateBookDTO updateBookDTO) throws Exception {
        BookEntity book = bookRepo.findById(updateBookDTO.getId())
                .orElseThrow(() -> new NotFoundException("Book not found"));
        book.setName(updateBookDTO.getName());
        book.setDescription(updateBookDTO.getDescription());
        book.setPublishYear(updateBookDTO.getPublishYear());
        book.setQuantity(updateBookDTO.getQuantity());

        List<AuthorEntity> authors = updateBookDTO.getAuthors().stream().map(author ->
                authorRepo.findById(author).orElseThrow(() -> new NotFoundException("Author not found with id: " + author))).collect(Collectors.toList());
        book.setAuthors(authors);

        List<CategoryEntity> categories = updateBookDTO.getCategories().stream().map(category ->
                categoryRepo.findById(category).orElseThrow(() -> new NotFoundException("Category not found with id: " + category))).collect(Collectors.toList());
        book.setCategories(categories);

        PublisherEntity publisher = publisherRepo.findById(updateBookDTO.getPublisherId())
                .orElseThrow(() -> new NotFoundException("Publisher not found"));
        book.setPublisher(publisher);

        return convertDTO(bookRepo.save(book));
    }

    @Override
    @Transactional(readOnly = true)
    public BookDTO getBook(Long id) throws Exception {
        BookEntity book = bookRepo.findById(id).orElseThrow(() -> new NotFoundException("Book not found"));
        return convertDTO(book);
    }

    @Override
    @Transactional(readOnly = true)
    public PageDTO getBooks(Specification<BookEntity> spec, Pageable pageable) throws Exception {
        Page<BookEntity> pageData = bookRepo.findAll(spec, pageable);
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(pageable.getPageNumber() + 1);
        pageDTO.setSize(pageable.getPageSize());
        pageDTO.setTotalElements(pageData.getTotalElements());
        pageDTO.setTotalPages(pageData.getTotalPages());
        List<BookDTO> bookDTOS = pageData.getContent()
                .stream()
                .map(BookServiceImpl::convertDTO)
                .toList();
        pageDTO.setData(bookDTOS);
        return pageDTO;
    }

    @Override
    public void deleteBook(Long id) throws Exception {
        BookEntity book = bookRepo.findById(id).orElseThrow(() -> new NotFoundException("Book not found"));
        book.setActive(false);
        bookRepo.save(book);
    }

    public static BookDTO convertDTO(BookEntity book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setName(book.getName());
        bookDTO.setDescription(book.getDescription());
        bookDTO.setPublishYear(book.getPublishYear());
        bookDTO.setQuantity(book.getQuantity());
        bookDTO.setCreatedAt(book.getCreatedAt());
        bookDTO.setUpdatedAt(book.getUpdatedAt());

        List<String> authors = book.getAuthors().stream()
                .map(AuthorEntity::getName)
                .collect(Collectors.toList());
        bookDTO.setAuthors(authors);

        List<String> categories = book.getCategories().stream()
                .map(CategoryEntity::getName)
                .collect(Collectors.toList());
        bookDTO.setCategories(categories);

        bookDTO.setPublisher(book.getPublisher().getName());
        return bookDTO;
    }
}
