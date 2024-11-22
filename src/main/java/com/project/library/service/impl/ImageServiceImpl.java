package com.project.library.service.impl;

import com.project.library.entity.ImageEntity;
import com.project.library.repository.BookRepo;
import com.project.library.repository.ImageRepo;
import com.project.library.service.ImageService;
import com.project.library.util.NotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepo imageRepo;

    @Autowired
    private BookRepo bookRepo;

    @Override
    public List<ImageEntity> saveImages(List<ImageEntity> images) throws Exception {
        return imageRepo.saveAll(images);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImageEntity> getBookImages(Long bookId) throws Exception {
        if (!bookRepo.existsById(bookId)) {
            throw new NotFoundException("Book id not found");
        }
        return imageRepo.findByBookId(bookId);
    }

    @Override
    public void deleteImage(Long id) throws Exception {
        ImageEntity image = imageRepo.findById(id).orElseThrow(() -> new NotFoundException("Image not found"));
        if (image.getBook() != null && image.getBook().getImages() != null && !image.getBook().getImages().isEmpty()) {
            image.getBook().getImages().remove(image);
        }
        imageRepo.deleteById(id);
    }
}
