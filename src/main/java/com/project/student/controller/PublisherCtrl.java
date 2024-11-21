package com.project.student.controller;

import com.project.student.dto.ResponseObject;
import com.project.student.dto.publisher.CreatePublisherDTO;
import com.project.student.dto.publisher.PublisherDTO;
import com.project.student.dto.publisher.UpdatePublisherDTO;
import com.project.student.service.PublisherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<ResponseObject> getPublishers() throws Exception {
        List<PublisherDTO> publishers = publisherService.getPublishers();
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Publishers fetched successfully", publishers, null
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
