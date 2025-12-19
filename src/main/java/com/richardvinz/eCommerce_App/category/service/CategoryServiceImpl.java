package com.richardvinz.eCommerce_App.category.service;

import com.richardvinz.eCommerce_App.category.exception.APIException;
import com.richardvinz.eCommerce_App.category.exception.ResourceNotFoundException;
import com.richardvinz.eCommerce_App.category.model.Category;
import com.richardvinz.eCommerce_App.category.payload.request.CategoryDTO;
import com.richardvinz.eCommerce_App.category.payload.response.CategoryResponse;
import com.richardvinz.eCommerce_App.category.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
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
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public CategoryResponse getAllCategories() {
        List<Category> categoryList = categoryRepository.findAll();
        if(categoryList.isEmpty()){
            throw new APIException("No category created yet!");
        }
        List<CategoryDTO> categories = categoryList.stream().map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();
        CategoryResponse response = new CategoryResponse();
        response.setContent(categories);
        return response;
    }

    @Override
    public CategoryDTO createCategories(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category existingCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if(existingCategory != null) {
        throw new APIException("Category with name: "+ category.getCategoryName()+ " already exist");
        }
           Category savedCategory = categoryRepository.save(category);
            return modelMapper.map(savedCategory, CategoryDTO.class);
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
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO,Category.class);

        Category existingCategory = categoryRepository.findById(categoryId).orElseThrow(
                ()-> new ResourceNotFoundException("Category","categoryId",categoryId)
        );
        existingCategory.setCategoryName(category.getCategoryName());
        Category updatedCategory = categoryRepository.save(existingCategory);
        return modelMapper.map(updatedCategory, CategoryDTO.class);
    }
}
