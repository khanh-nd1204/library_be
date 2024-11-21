package com.project.student.service.impl;

import com.project.student.dto.author.AuthorDTO;
import com.project.student.dto.author.CreateAuthorDTO;
import com.project.student.dto.author.UpdateAuthorDTO;
import com.project.student.entity.AuthorEntity;
import com.project.student.repository.AuthorRepo;
import com.project.student.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepo authorRepo;

    @Override
    public AuthorDTO createAuthor(CreateAuthorDTO createAuthorDTO) throws Exception {
        AuthorEntity author = new AuthorEntity();
        author.setName(createAuthorDTO.getName());
        return convertDTO(authorRepo.save(author));
    }

    @Override
    public AuthorDTO updateAuthor(UpdateAuthorDTO updateAuthorDTO) throws Exception {
        AuthorEntity author = authorRepo.findById(updateAuthorDTO.getId())
                .orElseThrow(() -> new Exception("Author not found"));
        author.setName(updateAuthorDTO.getName());
        return convertDTO(authorRepo.save(author));
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorDTO getAuthor(Long id) throws Exception {
        AuthorEntity author = authorRepo.findById(id).orElseThrow(() -> new Exception("Author not found"));
        return convertDTO(author);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorDTO> getAuthors() throws Exception {
        List<AuthorEntity> list = authorRepo.findAll();
        return list.stream()
                .map(AuthorServiceImpl::convertDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAuthor(Long id) throws Exception {
        AuthorEntity author = authorRepo.findById(id).orElseThrow(() -> new Exception("Author not found"));
        if (author.getBooks() != null && !author.getBooks().isEmpty()) {
            author.getBooks().forEach(book -> book.getAuthors().remove(author));
        }
        authorRepo.delete(author);
    }

    public static AuthorDTO convertDTO(AuthorEntity author) {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(author.getId());
        authorDTO.setName(author.getName());
        authorDTO.setCreatedAt(author.getCreatedAt());
        authorDTO.setUpdatedAt(author.getUpdatedAt());
        return authorDTO;
    }
}
