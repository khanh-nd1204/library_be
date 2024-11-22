package com.project.library.repository;

import com.project.library.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepo extends JpaRepository<ImageEntity, Long> {
    List<ImageEntity> findByBookId(Long bookId);
}
