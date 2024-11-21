package com.project.student.service.impl;

import com.project.student.dto.category.CategoryDTO;
import com.project.student.dto.category.CreateCategoryDTO;
import com.project.student.dto.category.UpdateCategoryDTO;
import com.project.student.entity.CategoryEntity;
import com.project.student.repository.CategoryRepo;
import com.project.student.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Override
    public CategoryDTO createCategory(CreateCategoryDTO createCategoryDTO) throws Exception {
        CategoryEntity category = new CategoryEntity();
        category.setName(createCategoryDTO.getName());
        return convertDTO(categoryRepo.save(category));
    }

    @Override
    public CategoryDTO updateCategory(UpdateCategoryDTO updateCategoryDTO) throws Exception {
        CategoryEntity category = categoryRepo.findById(updateCategoryDTO.getId())
                .orElseThrow(() -> new Exception("Category not found"));
        category.setName(updateCategoryDTO.getName());
        return convertDTO(categoryRepo.save(category));
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO getCategory(Long id) throws Exception {
        CategoryEntity category = categoryRepo.findById(id).orElseThrow(() -> new Exception("Category not found"));
        return convertDTO(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getCategories() throws Exception {
        List<CategoryEntity> list = categoryRepo.findAll();
        return list.stream()
                .map(CategoryServiceImpl::convertDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCategory(Long id) throws Exception {
        CategoryEntity category = categoryRepo.findById(id).orElseThrow(() -> new Exception("Category not found"));
        if (category.getBooks() != null && !category.getBooks().isEmpty()) {
            category.getBooks().forEach(book -> book.getCategories().remove(category));
        }
        categoryRepo.delete(category);
    }

    public static CategoryDTO convertDTO(CategoryEntity category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setCreatedAt(category.getCreatedAt());
        categoryDTO.setUpdatedAt(category.getUpdatedAt());
        return categoryDTO;
    }
}
