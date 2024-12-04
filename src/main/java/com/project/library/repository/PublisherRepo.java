package com.project.library.repository;

import com.project.library.entity.PublisherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepo extends JpaRepository<PublisherEntity, Long>, JpaSpecificationExecutor<PublisherEntity> {
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
}
