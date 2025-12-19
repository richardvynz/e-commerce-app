package com.richardvinz.eCommerce_App.category.controller;

import com.richardvinz.eCommerce_App.category.model.Category;
import com.richardvinz.eCommerce_App.category.payload.request.CategoryDTO;
import com.richardvinz.eCommerce_App.category.payload.response.CategoryResponse;
import com.richardvinz.eCommerce_App.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;



    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse>getCategories(){
        return new ResponseEntity<>(categoryService.getAllCategories(),HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<CategoryDTO> addCategories(@Valid @RequestBody CategoryDTO category){
       CategoryDTO response = categoryService.createCategories(category);
                return new ResponseEntity<>(response,HttpStatus.CREATED);
    }
    @DeleteMapping("/admin/categories/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") Long categoryId){
            String status = categoryService.deleteCategoryById(categoryId);
            return new ResponseEntity<>(status, HttpStatus.OK);

    }
    
    @PutMapping("/admin/categories/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable("id") Long categoryId, @RequestBody CategoryDTO category){
            CategoryDTO updatedCategory = categoryService.updateCategory(categoryId, category);
            return new ResponseEntity<>(updatedCategory, HttpStatus.OK);

    }
}
