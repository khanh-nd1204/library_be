package com.project.library.service;

import com.project.library.entity.ImageEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ImageService {
    List<ImageEntity> saveImages(List<ImageEntity> images) throws Exception;
    List<ImageEntity> getBookImages(Long bookId) throws Exception;
    void deleteImage(Long id) throws Exception;
}
