package com.project.library.repository;

import com.project.library.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<RoleEntity, Long>, JpaSpecificationExecutor<RoleEntity> {
    boolean existsByName(String name);
}
