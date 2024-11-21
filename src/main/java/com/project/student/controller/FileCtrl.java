package com.project.student.controller;

import com.project.student.dto.FileDTO;
import com.project.student.dto.ResponseObject;
import com.project.student.service.FileService;
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
        FileDTO fileDTO = fileService.store(file, folder);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "File upload successfully", fileDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
