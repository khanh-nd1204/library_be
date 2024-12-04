package com.project.library.service.impl;

import com.project.library.dto.PageDTO;
import com.project.library.dto.author.AuthorDTO;
import com.project.library.dto.publisher.CreatePublisherDTO;
import com.project.library.dto.publisher.PublisherDTO;
import com.project.library.dto.publisher.UpdatePublisherDTO;
import com.project.library.entity.AuthorEntity;
import com.project.library.entity.PublisherEntity;
import com.project.library.repository.PublisherRepo;
import com.project.library.service.PublisherService;
import com.project.library.util.NotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublisherServiceImpl implements PublisherService {

    @Autowired
    private PublisherRepo publisherRepo;

    @Override
    public PublisherDTO createPublisher(CreatePublisherDTO createPublisherDTO) throws Exception {
        if (publisherRepo.existsByPhone(createPublisherDTO.getPhone())) {
            throw new BadRequestException("Phone is already exist");
        }

        if (publisherRepo.existsByEmail(createPublisherDTO.getEmail())) {
            throw new BadRequestException("Email is already exist");
        }

        PublisherEntity publisher = new PublisherEntity();
        publisher.setName(createPublisherDTO.getName().trim());
        publisher.setAddress(createPublisherDTO.getAddress().trim());
        publisher.setEmail(createPublisherDTO.getEmail().trim());
        publisher.setPhone(createPublisherDTO.getPhone().trim());
        return convertDTO(publisherRepo.save(publisher));
    }

    @Override
    public PublisherDTO updatePublisher(UpdatePublisherDTO updatePublisherDTO) throws Exception {
        PublisherEntity publisher = publisherRepo.findById(updatePublisherDTO.getId())
                .orElseThrow(() -> new NotFoundException("Publisher not found"));
        if (publisherRepo.existsByPhone(updatePublisherDTO.getPhone()) && !publisher.getPhone().equals(updatePublisherDTO.getPhone())) {
            throw new BadRequestException("Phone is already exist");
        }
        if (publisherRepo.existsByEmail(updatePublisherDTO.getEmail()) && !publisher.getEmail().equals(updatePublisherDTO.getEmail())) {
            throw new BadRequestException("Email is already exist");
        }
        publisher.setName(updatePublisherDTO.getName().trim());
        publisher.setAddress(updatePublisherDTO.getAddress().trim());
        publisher.setPhone(updatePublisherDTO.getPhone().trim());
        publisher.setEmail(updatePublisherDTO.getEmail().trim());
        return convertDTO(publisherRepo.save(publisher));
    }

    @Override
    @Transactional(readOnly = true)
    public PublisherDTO getPublisher(Long id) throws Exception {
        PublisherEntity publisher = publisherRepo.findById(id).orElseThrow(() -> new NotFoundException("Publisher not found"));
        return convertDTO(publisher);
    }

    @Override
    public PageDTO getPublishers(Specification<PublisherEntity> spec, Pageable pageable) throws Exception {
        Page<PublisherEntity> pageData = publisherRepo.findAll(spec, pageable);
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(pageable.getPageNumber() + 1);
        pageDTO.setSize(pageable.getPageSize());
        pageDTO.setTotalElements(pageData.getTotalElements());
        pageDTO.setTotalPages(pageData.getTotalPages());
        List<PublisherDTO> publisherDTOS = pageData.getContent()
                .stream()
                .map(PublisherServiceImpl::convertDTO)
                .toList();
        pageDTO.setData(publisherDTOS);
        return pageDTO;
    }


    @Override
    public void deletePublisher(Long id) throws Exception {
        PublisherEntity publisher = publisherRepo.findById(id).orElseThrow(() -> new NotFoundException("Publisher not found"));
        if (publisher.getBooks() != null && !publisher.getBooks().isEmpty()) {
            publisher.getBooks().forEach(book -> book.setPublisher(null));
        }
        publisherRepo.delete(publisher);
    }

    public static PublisherDTO convertDTO(PublisherEntity publisher) {
        PublisherDTO publisherDTO = new PublisherDTO();
        publisherDTO.setId(publisher.getId());
        publisherDTO.setName(publisher.getName());
        publisherDTO.setAddress(publisher.getAddress());
        publisherDTO.setEmail(publisher.getEmail());
        publisherDTO.setPhone(publisher.getPhone());
        publisherDTO.setCreatedAt(publisher.getCreatedAt());
        publisherDTO.setUpdatedAt(publisher.getUpdatedAt());
        return publisherDTO;
    }
}
