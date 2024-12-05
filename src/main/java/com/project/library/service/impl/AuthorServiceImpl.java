package com.project.library.service.impl;

import com.project.library.dto.PageDTO;
import com.project.library.dto.author.AuthorDTO;
import com.project.library.dto.author.CreateAuthorDTO;
import com.project.library.dto.author.UpdateAuthorDTO;
import com.project.library.entity.AuthorEntity;
import com.project.library.repository.AuthorRepo;
import com.project.library.service.AuthorService;
import com.project.library.util.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepo authorRepo;

    @Override
    public AuthorDTO createAuthor(CreateAuthorDTO createAuthorDTO) throws Exception {
        AuthorEntity author = new AuthorEntity();
        author.setName(createAuthorDTO.getName().trim());
        author.setNationality(createAuthorDTO.getNationality().trim());
        author.setPenName(createAuthorDTO.getPenName().trim());
        return convertDTO(authorRepo.save(author));
    }

    @Override
    public AuthorDTO updateAuthor(UpdateAuthorDTO updateAuthorDTO) throws Exception {
        AuthorEntity author = authorRepo.findById(updateAuthorDTO.getId())
                .orElseThrow(() -> new NotFoundException("Author not found"));
        author.setName(updateAuthorDTO.getName().trim());
        author.setNationality(updateAuthorDTO.getNationality().trim());
        author.setPenName(updateAuthorDTO.getPenName().trim());
        return convertDTO(authorRepo.save(author));
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorDTO getAuthor(Long id) throws Exception {
        AuthorEntity author = authorRepo.findById(id).orElseThrow(() -> new NotFoundException("Author not found"));
        return convertDTO(author);
    }

    @Override
    @Transactional(readOnly = true)
    public PageDTO getAuthors(Specification<AuthorEntity> spec, Pageable pageable) throws Exception {
        Page<AuthorEntity> pageData = authorRepo.findAll(spec, pageable);
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(pageable.getPageNumber() + 1);
        pageDTO.setSize(pageable.getPageSize());
        pageDTO.setTotalElements(pageData.getTotalElements());
        pageDTO.setTotalPages(pageData.getTotalPages());
        List<AuthorDTO> authorDTOS = pageData.getContent()
                .stream()
                .map(AuthorServiceImpl::convertDTO)
                .toList();
        pageDTO.setData(authorDTOS);
        return pageDTO;
    }


    @Override
    public void deleteAuthor(Long id) throws Exception {
        AuthorEntity author = authorRepo.findById(id).orElseThrow(() -> new NotFoundException("Author not found"));
        if (author.getBooks() != null && !author.getBooks().isEmpty()) {
            author.getBooks().forEach(book -> book.getAuthors().remove(author));
        }
        authorRepo.delete(author);
    }

    public static AuthorDTO convertDTO(AuthorEntity author) {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(author.getId());
        authorDTO.setName(author.getName());
        authorDTO.setNationality(author.getNationality());
        authorDTO.setPenName(author.getPenName());
        authorDTO.setCreatedAt(author.getCreatedAt());
        authorDTO.setUpdatedAt(author.getUpdatedAt());
        return authorDTO;
    }
}
