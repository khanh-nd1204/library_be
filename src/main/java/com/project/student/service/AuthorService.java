package com.project.student.service;

import com.project.student.dto.author.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuthorService {
    AuthorDTO createAuthor(CreateAuthorDTO createAuthorDTO) throws Exception;
    AuthorDTO updateAuthor(UpdateAuthorDTO updateAuthorDTO) throws Exception;
    AuthorDTO getAuthor(Long id) throws Exception;
    List<AuthorDTO> getAuthors() throws Exception;
    void deleteAuthor(Long id) throws Exception;
}
