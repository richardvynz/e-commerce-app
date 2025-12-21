package com.richardvinz.eCommerce_App.category.service;

import com.richardvinz.eCommerce_App.category.model.Category;
import com.richardvinz.eCommerce_App.category.payload.request.CategoryDTO;
import com.richardvinz.eCommerce_App.category.payload.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    CategoryDTO createCategories(CategoryDTO category);

    String deleteCategoryById(Long categoryId);

    CategoryDTO updateCategory(Long categoryId, CategoryDTO category);

}
