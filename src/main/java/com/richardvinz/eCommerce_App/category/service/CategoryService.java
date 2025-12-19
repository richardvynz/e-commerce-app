package com.richardvinz.eCommerce_App.category.service;

import com.richardvinz.eCommerce_App.category.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    String createCategories(Category category);

    String deleteCategoryById(Long categoryId);

    Category updateCategory(Long categoryId, Category category);
}
