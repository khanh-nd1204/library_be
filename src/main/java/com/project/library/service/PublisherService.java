package com.project.library.service;

import com.project.library.dto.publisher.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PublisherService {
    PublisherDTO createPublisher(CreatePublisherDTO createPublisherDTO) throws Exception;
    PublisherDTO updatePublisher(UpdatePublisherDTO updatePublisherDTO) throws Exception;
    PublisherDTO getPublisher(Long id) throws Exception;
    List<PublisherDTO> getPublishers() throws Exception;
    void deletePublisher(Long id) throws Exception;
}
