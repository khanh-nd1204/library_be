package com.project.library.service;

import com.project.library.dto.PageDTO;
import com.project.library.dto.publisher.*;
import com.project.library.entity.PublisherEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public interface PublisherService {
    PublisherDTO createPublisher(CreatePublisherDTO createPublisherDTO) throws Exception;
    PublisherDTO updatePublisher(UpdatePublisherDTO updatePublisherDTO) throws Exception;
    PublisherDTO getPublisher(Long id) throws Exception;
    PageDTO getPublishers(Specification<PublisherEntity> spec, Pageable pageable) throws Exception;
    void deletePublisher(Long id) throws Exception;
}
