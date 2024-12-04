package com.project.library.service;

import com.project.library.dto.PageDTO;
import com.project.library.dto.category.*;
import com.project.library.entity.CategoryEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {
    CategoryDTO createCategory(CreateCategoryDTO createCategoryDTO) throws Exception;
    CategoryDTO updateCategory(UpdateCategoryDTO updateCategoryDTO) throws Exception;
    CategoryDTO getCategory(Long id) throws Exception;
    PageDTO getCategories(Specification<CategoryEntity> spec, Pageable pageable) throws Exception;
    void deleteCategory(Long id) throws Exception;
}
