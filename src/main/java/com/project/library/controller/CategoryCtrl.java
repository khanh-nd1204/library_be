package com.project.library.controller;

import com.project.library.dto.PageDTO;
import com.project.library.dto.ResponseObject;
import com.project.library.dto.category.CategoryDTO;
import com.project.library.dto.category.CreateCategoryDTO;
import com.project.library.dto.category.UpdateCategoryDTO;
import com.project.library.entity.CategoryEntity;
import com.project.library.service.CategoryService;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryCtrl {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ResponseObject> createCategory(@Valid @RequestBody CreateCategoryDTO createCategoryDTO) throws Exception {
        CategoryDTO categoryDTO = categoryService.createCategory(createCategoryDTO);
        ResponseObject res = new ResponseObject(HttpStatus.CREATED.value(),
                "Category created successfully", categoryDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<ResponseObject> updateCategory(@Valid @RequestBody UpdateCategoryDTO updateCategoryDTO) throws Exception {
        CategoryDTO categoryDTO = categoryService.updateCategory(updateCategoryDTO);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Category updated successfully", categoryDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ResponseObject> getCategories(
            @Filter Specification<CategoryEntity> spec, Pageable pageable) throws Exception {
        PageDTO pageDTO = categoryService.getCategories(spec, pageable);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Categories fetched successfully", pageDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getCategory(@PathVariable Long id) throws Exception {
        CategoryDTO categoryDTO = categoryService.getCategory(id);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Category fetched successfully", categoryDTO, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteCategory(@PathVariable Long id) throws Exception {
        categoryService.deleteCategory(id);
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Category deleted successfully", null, null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
