package com.project.library.controller;

import com.project.library.dto.ImageDTO;
import com.project.library.dto.ResponseObject;
import com.project.library.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/file")
public class FileCtrl {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<ResponseObject> uploadFile(
            @RequestParam("file") MultipartFile file, @RequestParam("folder") String folder) throws Exception {
        fileService.validate(file, folder);
        ImageDTO imageDTO = fileService.store(file, folder);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "File upload successfully", imageDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
