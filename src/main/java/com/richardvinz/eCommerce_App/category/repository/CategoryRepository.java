package com.richardvinz.eCommerce_App.category.repository;

import com.richardvinz.eCommerce_App.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Category findByCategoryName(String categoryName);
}
