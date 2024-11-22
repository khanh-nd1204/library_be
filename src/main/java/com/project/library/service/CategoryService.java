package com.project.library.service;

import com.project.library.dto.category.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    CategoryDTO createCategory(CreateCategoryDTO createCategoryDTO) throws Exception;
    CategoryDTO updateCategory(UpdateCategoryDTO updateCategoryDTO) throws Exception;
    CategoryDTO getCategory(Long id) throws Exception;
    List<CategoryDTO> getCategories() throws Exception;
    void deleteCategory(Long id) throws Exception;
}
