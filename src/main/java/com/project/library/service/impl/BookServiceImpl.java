package com.project.library.service.impl;

import com.project.library.dto.ImageDTO;
import com.project.library.dto.PageDTO;
import com.project.library.dto.book.*;
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

        List<ImageEntity> images = createBookDTO.getImages().stream().map(imageId -> {
            ImageEntity image = imageRepo.findById(imageId)
                    .orElseThrow(() -> new NotFoundException("Image not found with id: " + imageId));
            image.setBook(book);
            return image;
        }).collect(Collectors.toList());
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

        List<ImageEntity> images = updateBookDTO.getImages().stream().map(imageId -> {
            ImageEntity image = imageRepo.findById(imageId)
                    .orElseThrow(() -> new NotFoundException("Image not found with id: " + imageId));
            image.setBook(book);
            return image;
        }).collect(Collectors.toList());
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

        List<BookAuthorDTO> authors = new ArrayList<>();
        book.getAuthors().forEach(author -> {
            BookAuthorDTO authorDTO = new BookAuthorDTO();
            authorDTO.setId(author.getId());
            authorDTO.setName(author.getName());
            authors.add(authorDTO);
        });
        bookDTO.setAuthors(authors);

        List<BookCategoryDTO> categories = new ArrayList<>();
        book.getCategories().forEach(category -> {
            BookCategoryDTO categoryDTO = new BookCategoryDTO();
            categoryDTO.setId(category.getId());
            categoryDTO.setName(category.getName());
            categories.add(categoryDTO);
        });
        bookDTO.setCategories(categories);

        List<ImageDTO> images = new ArrayList<>();
        book.getImages().forEach(image -> {
            ImageDTO imageDTO = new ImageDTO();
            imageDTO.setId(image.getId());
            imageDTO.setImageUrl(image.getImageUrl());
            imageDTO.setFolder(image.getFolder());
            images.add(imageDTO);
        });
        bookDTO.setImages(images);

        BookPublisherDTO publisherDTO = new BookPublisherDTO();
        publisherDTO.setId(book.getPublisher().getId());
        publisherDTO.setName(book.getPublisher().getName());
        bookDTO.setPublisher(publisherDTO);
        return bookDTO;
    }
}
