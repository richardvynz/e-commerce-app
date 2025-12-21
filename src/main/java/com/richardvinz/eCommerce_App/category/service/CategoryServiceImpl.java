package com.richardvinz.eCommerce_App.category.service;

import com.richardvinz.eCommerce_App.category.exception.APIException;
import com.richardvinz.eCommerce_App.category.exception.ResourceNotFoundException;
import com.richardvinz.eCommerce_App.category.model.Category;
import com.richardvinz.eCommerce_App.category.payload.request.CategoryDTO;
import com.richardvinz.eCommerce_App.category.payload.response.CategoryResponse;
import com.richardvinz.eCommerce_App.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    @Override
    public CategoryResponse getAllCategories(Integer pageNumber,
                                             Integer pageSize,
                                             String sortBy,
                                             String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);

        List<Category> categoryList = categoryPage.getContent();
        if(categoryList.isEmpty()){
            throw new APIException("THERE ARE NO CATEGORIES PRESENT!");
        }
        List<CategoryDTO> categories = categoryList.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();

        CategoryResponse response = new CategoryResponse();
        response.setContent(categories);
        response.setLastPage(categoryPage.isLast());
        response.setPageNumber(categoryPage.getNumber());
        response.setPageSize(categoryPage.getSize());
        response.setTotalElements(categoryPage.getTotalElements());
        response.setTotalPages(categoryPage.getTotalPages());
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
