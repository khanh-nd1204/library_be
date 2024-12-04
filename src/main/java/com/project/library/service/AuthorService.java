package com.project.library.service;

import com.project.library.dto.PageDTO;
import com.project.library.dto.author.*;
import com.project.library.entity.AuthorEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuthorService {
    AuthorDTO createAuthor(CreateAuthorDTO createAuthorDTO) throws Exception;
    AuthorDTO updateAuthor(UpdateAuthorDTO updateAuthorDTO) throws Exception;
    AuthorDTO getAuthor(Long id) throws Exception;
    PageDTO getAuthors(Specification<AuthorEntity> spec, Pageable pageable) throws Exception;
    void deleteAuthor(Long id) throws Exception;
}
