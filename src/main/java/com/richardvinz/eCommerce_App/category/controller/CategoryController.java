package com.richardvinz.eCommerce_App.category.controller;

import com.richardvinz.eCommerce_App.category.config.AppConstants;
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

import static com.richardvinz.eCommerce_App.category.config.AppConstants.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;



    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse>getCategories(@RequestParam(name = "pageNumber",required = false,defaultValue = PAGE_NUMBER) Integer pageNumber,
                                                         @RequestParam(name = "pageSize",required = false,defaultValue = PAGE_SIZE) Integer pageSize,
                                                         @RequestParam(name = "sortBy",defaultValue = SORT_BY,required = false) String sortBy,
                                                         @RequestParam(name = "sortOrder",defaultValue = SORT_ORDER, required = false) String sortOrder){
        return new ResponseEntity<>(categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder),HttpStatus.OK);
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
