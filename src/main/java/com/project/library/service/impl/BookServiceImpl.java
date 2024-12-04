package com.project.library.service.impl;

import com.project.library.dto.PageDTO;
import com.project.library.dto.book.BookDTO;
import com.project.library.dto.book.CreateBookDTO;
import com.project.library.dto.book.UpdateBookDTO;
import com.project.library.entity.*;
import com.project.library.repository.*;
import com.project.library.service.BookService;
import com.project.library.util.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private ImageRepo imageRepo;

    @Override
    public BookDTO createBook(CreateBookDTO createBookDTO) throws Exception {
        BookEntity book = new BookEntity();
        book.setName(createBookDTO.getName().trim());
        book.setDescription(createBookDTO.getDescription().trim());
        book.setPublishYear(createBookDTO.getPublishYear());
        book.setQuantity(createBookDTO.getQuantity());
        book.setActive(true);

        List<AuthorEntity> authors = createBookDTO.getAuthors().stream().map(author ->
                authorRepo.findById(author).orElseThrow(() -> new NotFoundException("Author not found with id: " + author))).collect(Collectors.toList());
        book.setAuthors(authors);

        List<CategoryEntity> categories = createBookDTO.getCategories().stream().map(category ->
                categoryRepo.findById(category).orElseThrow(() -> new NotFoundException("Category not found with id: " + category))).collect(Collectors.toList());
        book.setCategories(categories);

        List<ImageEntity> images = createBookDTO.getImages().stream().map(image ->
                imageRepo.findById(image).orElseThrow(() -> new NotFoundException("Image not found with id: " + image))).collect(Collectors.toList());
        book.setImages(images);

        PublisherEntity publisher = publisherRepo.findById(createBookDTO.getPublisherId())
                .orElseThrow(() -> new NotFoundException("Publisher not found"));
        book.setPublisher(publisher);

        return convertDTO(bookRepo.save(book));
    }

    @Override
    public BookDTO updateBook(UpdateBookDTO updateBookDTO) throws Exception {
        BookEntity book = bookRepo.findById(updateBookDTO.getId())
                .orElseThrow(() -> new NotFoundException("Book not found"));
        book.setName(updateBookDTO.getName().trim());
        book.setDescription(updateBookDTO.getDescription().trim());
        book.setPublishYear(updateBookDTO.getPublishYear());
        book.setQuantity(updateBookDTO.getQuantity());

        List<AuthorEntity> authors = updateBookDTO.getAuthors().stream().map(author ->
                authorRepo.findById(author).orElseThrow(() -> new NotFoundException("Author not found with id: " + author))).collect(Collectors.toList());
        book.setAuthors(authors);

        List<CategoryEntity> categories = updateBookDTO.getCategories().stream().map(category ->
                categoryRepo.findById(category).orElseThrow(() -> new NotFoundException("Category not found with id: " + category))).collect(Collectors.toList());
        book.setCategories(categories);

        List<ImageEntity> images = updateBookDTO.getImages().stream().map(image ->
                imageRepo.findById(image).orElseThrow(() -> new NotFoundException("Image not found with id: " + image))).collect(Collectors.toList());
        book.setImages(images);

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
        bookDTO.setActive(book.isActive());
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

        List<String> images = book.getImages().stream()
                .map(ImageEntity::getImageUrl)
                .collect(Collectors.toList());
        bookDTO.setImages(images);

        bookDTO.setPublisher(book.getPublisher().getName());
        return bookDTO;
    }
}
