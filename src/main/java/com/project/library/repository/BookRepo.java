package com.project.library.repository;

import com.project.library.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookRepo extends JpaRepository<BookEntity, Long>, JpaSpecificationExecutor<BookEntity> {
}
