package com.richardvinz.eCommerce_App.product.repository;

import com.richardvinz.eCommerce_App.category.model.Category;
import com.richardvinz.eCommerce_App.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    Page<Product> findByCategoryOrderByPriceAsc(Category category, Pageable pageDetail);

    Page<Product> findByProductNameLikeIgnoreCase(String s, Pageable pageDetails);
}
