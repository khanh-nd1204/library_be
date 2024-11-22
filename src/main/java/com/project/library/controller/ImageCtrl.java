package com.project.library.controller;

import com.project.library.dto.ResponseObject;
import com.project.library.entity.ImageEntity;
import com.project.library.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/images")
public class ImageCtrl {

    @Autowired
    private ImageService imageService;

    @PostMapping
    public ResponseEntity<ResponseObject> saveImages(@RequestBody List<ImageEntity> images) throws Exception {
        List<ImageEntity> imageList = imageService.saveImages(images);
        ResponseObject res = new ResponseObject(HttpStatus.CREATED.value(),
                "Book created successfully", imageList, null
        );
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getImages(@PathVariable Long id) throws Exception {
        List<ImageEntity> imageList = imageService.getBookImages(id);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Book images fetched successfully", imageList, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteImage(@PathVariable Long id) throws Exception {
        imageService.deleteImage(id);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Image deleted successfully", null, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}
