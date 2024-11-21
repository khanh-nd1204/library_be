package com.project.student.service.impl;

import com.project.student.dto.publisher.CreatePublisherDTO;
import com.project.student.dto.publisher.PublisherDTO;
import com.project.student.dto.publisher.UpdatePublisherDTO;
import com.project.student.entity.PublisherEntity;
import com.project.student.repository.PublisherRepo;
import com.project.student.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
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
        PublisherEntity publisher = new PublisherEntity();
        publisher.setName(createPublisherDTO.getName());
        return convertDTO(publisherRepo.save(publisher));
    }

    @Override
    public PublisherDTO updatePublisher(UpdatePublisherDTO updatePublisherDTO) throws Exception {
        PublisherEntity publisher = publisherRepo.findById(updatePublisherDTO.getId())
                .orElseThrow(() -> new Exception("Publisher not found"));
        publisher.setName(updatePublisherDTO.getName());
        return convertDTO(publisherRepo.save(publisher));
    }

    @Override
    @Transactional(readOnly = true)
    public PublisherDTO getPublisher(Long id) throws Exception {
        PublisherEntity publisher = publisherRepo.findById(id).orElseThrow(() -> new Exception("Publisher not found"));
        return convertDTO(publisher);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PublisherDTO> getPublishers() throws Exception {
        List<PublisherEntity> list = publisherRepo.findAll();
        return list.stream()
                .map(PublisherServiceImpl::convertDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deletePublisher(Long id) throws Exception {
        PublisherEntity publisher = publisherRepo.findById(id).orElseThrow(() -> new Exception("Publisher not found"));
        if (publisher.getBooks() != null && !publisher.getBooks().isEmpty()) {
            publisher.getBooks().forEach(book -> book.setPublisher(null));
        }
        publisherRepo.delete(publisher);
    }

    public static PublisherDTO convertDTO(PublisherEntity publisher) {
        PublisherDTO publisherDTO = new PublisherDTO();
        publisherDTO.setId(publisher.getId());
        publisherDTO.setName(publisher.getName());
        publisherDTO.setCreatedAt(publisher.getCreatedAt());
        publisherDTO.setUpdatedAt(publisher.getUpdatedAt());
        return publisherDTO;
    }
}
