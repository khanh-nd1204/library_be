package com.project.library.service.impl;

import com.project.library.dto.PageDTO;
import com.project.library.dto.category.CategoryDTO;
import com.project.library.dto.category.CreateCategoryDTO;
import com.project.library.dto.category.UpdateCategoryDTO;
import com.project.library.entity.CategoryEntity;
import com.project.library.repository.CategoryRepo;
import com.project.library.service.CategoryService;
import com.project.library.util.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Override
    public CategoryDTO createCategory(CreateCategoryDTO createCategoryDTO) throws Exception {
        CategoryEntity category = new CategoryEntity();
        category.setName(createCategoryDTO.getName().trim());
        category.setDescription(createCategoryDTO.getDescription().trim());
        return convertDTO(categoryRepo.save(category));
    }

    @Override
    public CategoryDTO updateCategory(UpdateCategoryDTO updateCategoryDTO) throws Exception {
        CategoryEntity category = categoryRepo.findById(updateCategoryDTO.getId())
                .orElseThrow(() -> new NotFoundException("Category not found"));
        category.setName(updateCategoryDTO.getName().trim());
        category.setDescription(updateCategoryDTO.getDescription().trim());
        return convertDTO(categoryRepo.save(category));
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO getCategory(Long id) throws Exception {
        CategoryEntity category = categoryRepo.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
        return convertDTO(category);
    }

    @Override
    @Transactional(readOnly = true)
    public PageDTO getCategories(Specification<CategoryEntity> spec, Pageable pageable) throws Exception {
        Page<CategoryEntity> pageData = categoryRepo.findAll(spec, pageable);
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(pageable.getPageNumber() + 1);
        pageDTO.setSize(pageable.getPageSize());
        pageDTO.setTotalElements(pageData.getTotalElements());
        pageDTO.setTotalPages(pageData.getTotalPages());
        List<CategoryDTO> categoryDTOS = pageData.getContent()
                .stream()
                .map(CategoryServiceImpl::convertDTO)
                .toList();
        pageDTO.setData(categoryDTOS);
        return pageDTO;
    }


    @Override
    public void deleteCategory(Long id) throws Exception {
        CategoryEntity category = categoryRepo.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
        if (category.getBooks() != null && !category.getBooks().isEmpty()) {
            category.getBooks().forEach(book -> book.getCategories().remove(category));
        }
        categoryRepo.delete(category);
    }

    public static CategoryDTO convertDTO(CategoryEntity category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setDescription(category.getDescription());
        categoryDTO.setCreatedAt(category.getCreatedAt());
        categoryDTO.setUpdatedAt(category.getUpdatedAt());
        return categoryDTO;
    }
}
