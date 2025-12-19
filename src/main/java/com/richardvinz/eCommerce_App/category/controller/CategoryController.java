package com.richardvinz.eCommerce_App.category.controller;

import com.richardvinz.eCommerce_App.category.model.Category;
import com.richardvinz.eCommerce_App.category.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping("/public/categories")
    public ResponseEntity<List<Category>>getCategories(){
        return new ResponseEntity<>(categoryService.getAllCategories(),HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<String> addCategories(@Valid @RequestBody Category category){
        return new ResponseEntity<>(categoryService.createCategories(category),HttpStatus.CREATED);
    }
    @DeleteMapping("/admin/categories/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") Long categoryId){
            String status = categoryService.deleteCategoryById(categoryId);
            return new ResponseEntity<>(status, HttpStatus.OK);

    }
    
    @PutMapping("/admin/categories/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable("id") Long categoryId, @RequestBody Category category){
            Category updatedCategory = categoryService.updateCategory(categoryId, category);
            return new ResponseEntity<>("category with id: " + categoryId + " updated successfully!", HttpStatus.OK);

    }
}
