package com.project.library.repository;

import com.project.library.entity.BorrowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowRepo extends JpaRepository<BorrowEntity, Long>, JpaSpecificationExecutor<BorrowEntity> {

}
