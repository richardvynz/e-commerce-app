package com.richardvinz.eCommerce_App.category.service;

import com.richardvinz.eCommerce_App.category.exception.APIException;
import com.richardvinz.eCommerce_App.category.exception.ResourceNotFoundException;
import com.richardvinz.eCommerce_App.category.model.Category;
import com.richardvinz.eCommerce_App.category.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public List<Category> getAllCategories() {
        List<Category> categoryList = categoryRepository.findAll();
        if(categoryList.isEmpty()){
            throw new APIException("No category created yet!");
        }
        return categoryList;
    }

    @Override
    public String createCategories(Category category) {
        Category existingCategory = categoryRepository.findByCategoryName(category.getCategoryName());

        if(existingCategory == null) {
            categoryRepository.save(category);
            return "category added successfully";
        }
        throw new APIException("Category with name: "+category.getCategoryName()+ " already exist");
    }

    @Override
    public String deleteCategoryById(Long categoryId) {
        Category existingCategory = categoryRepository.findById(categoryId).orElseThrow(
                ()-> new ResourceNotFoundException("Category", "categoryId",categoryId)
        );
        categoryRepository.delete(existingCategory);
        return "category with id: "+ categoryId +" is deleted successfully!";
    }

    @Override
    public Category updateCategory(Long categoryId, Category category) {
        Category existingCategory = categoryRepository.findById(categoryId).orElseThrow(
                ()-> new ResourceNotFoundException("Category","categoryId",categoryId)
        );
        existingCategory.setCategoryName(category.getCategoryName());
       return categoryRepository.save(existingCategory);
    }
}
