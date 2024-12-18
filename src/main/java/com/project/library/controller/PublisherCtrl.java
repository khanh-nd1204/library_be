package com.project.library.controller;

import com.project.library.dto.PageDTO;
import com.project.library.dto.ResponseObject;
import com.project.library.dto.publisher.CreatePublisherDTO;
import com.project.library.dto.publisher.PublisherDTO;
import com.project.library.dto.publisher.UpdatePublisherDTO;
import com.project.library.entity.PublisherEntity;
import com.project.library.service.PublisherService;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/publishers")
public class PublisherCtrl {

    @Autowired
    private PublisherService publisherService;

    @PostMapping
    public ResponseEntity<ResponseObject> createPublisher(@Valid @RequestBody CreatePublisherDTO createPublisherDTO) throws Exception {
        PublisherDTO publisherDTO = publisherService.createPublisher(createPublisherDTO);
        ResponseObject res = new ResponseObject(HttpStatus.CREATED.value(),
                "Publisher created successfully", publisherDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<ResponseObject> updatePublisher(@Valid @RequestBody UpdatePublisherDTO updatePublisherDTO) throws Exception {
        PublisherDTO publisherDTO = publisherService.updatePublisher(updatePublisherDTO);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Publisher updated successfully", publisherDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ResponseObject> getPublishers(
            @Filter Specification<PublisherEntity> spec, Pageable pageable) throws Exception {
        PageDTO pageDTO = publisherService.getPublishers(spec, pageable);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Publishers fetched successfully", pageDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getPublisher(@PathVariable Long id) throws Exception {
        PublisherDTO publisherDTO = publisherService.getPublisher(id);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Publisher fetched successfully", publisherDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deletePublisher(@PathVariable Long id) throws Exception {
        publisherService.deletePublisher(id);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Publisher deleted successfully", null, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
