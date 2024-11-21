package com.project.student.repository;

import com.project.student.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<RoleEntity, Long>, JpaSpecificationExecutor<RoleEntity> {
    boolean existsByName(String name);
}
