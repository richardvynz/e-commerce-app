package com.richardvinz.eCommerce_App.product.service;

import com.richardvinz.eCommerce_App.product.dto.ProductDTO;
import com.richardvinz.eCommerce_App.product.dto.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO createProduct(ProductDTO productDTO, Long categoryId);


    ProductResponse getProductList(Integer pageNumber, Integer pageSize, String sortOrder, String sortBy);

    ProductResponse searchProductsByCategory(Integer pageNumber, Integer pageSize, String sortOrder, String sortBy, Long categoryId);

    ProductResponse searchProductByKeyword(Integer pageNumber, Integer pageSize, String sortOrder, String sortBy, String keyword);

    ProductDTO updateProduct(Long productId, ProductDTO productDTO);

    ProductDTO deleteProduct(Long productId);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
