package com.project.student.repository;

import com.project.student.entity.BookCopyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookCopyRepo extends JpaRepository<BookCopyEntity, Long>, JpaSpecificationExecutor<BookCopyEntity> {
}
