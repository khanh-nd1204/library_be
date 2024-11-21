package com.project.student.controller;

import com.project.student.dto.ResponseObject;
import com.project.student.dto.category.CategoryDTO;
import com.project.student.dto.category.CreateCategoryDTO;
import com.project.student.dto.category.UpdateCategoryDTO;
import com.project.student.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<ResponseObject> getCategories() throws Exception {
        List<CategoryDTO> categories = categoryService.getCategories();
        ResponseObject res = new ResponseObject(HttpStatus.OK.value(),
                "Categories fetched successfully", categories, null
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
