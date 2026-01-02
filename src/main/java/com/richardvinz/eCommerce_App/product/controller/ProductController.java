package com.richardvinz.eCommerce_App.product.controller;

import com.richardvinz.eCommerce_App.category.model.Category;
import com.richardvinz.eCommerce_App.product.dto.ProductDTO;
import com.richardvinz.eCommerce_App.product.dto.ProductResponse;
import com.richardvinz.eCommerce_App.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.richardvinz.eCommerce_App.category.config.AppConstants.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/products")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable Long categoryId){
        ProductDTO response = productService.createProduct(productDTO, categoryId);

        return new ResponseEntity<>(response,CREATED);
    }
    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse>getAllProducts(
            @RequestParam(name = "pageNumber",defaultValue = PAGE_NUMBER, required = false) Integer pageNumber,
    @RequestParam(name = "pageSize",defaultValue = PAGE_SIZE, required = false) Integer pageSize,
    @RequestParam(name = "sortOrder",defaultValue = SORT_ORDER, required = false) String sortOrder,
    @RequestParam(name = "sortBy",defaultValue = SORT_PRODUCT_BY, required = false) String sortBy){
        ProductResponse response = productService.getProductList(pageNumber,pageSize,sortOrder,sortBy);
        return new ResponseEntity<>(response,OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> searchProductsByCategory( @RequestParam(name = "pageNumber",defaultValue = PAGE_NUMBER, required = false) Integer pageNumber,
                                                                     @RequestParam(name = "pageSize",defaultValue = PAGE_SIZE, required = false) Integer pageSize,
                                                                     @RequestParam(name = "sortOrder",defaultValue = SORT_ORDER, required = false) String sortOrder,
                                                                     @RequestParam(name = "sortBy",defaultValue = SORT_PRODUCT_BY, required = false) String sortBy,
                                                                     @PathVariable Long categoryId){
        ProductResponse response = productService.searchProductsByCategory(pageNumber,pageSize,sortOrder,sortBy,categoryId);
        return new ResponseEntity<>(response,OK);

    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> searchProductByKeyword(@RequestParam(name = "pageNumber",defaultValue = PAGE_NUMBER, required = false) Integer pageNumber,
                                                                  @RequestParam(name = "pageSize",defaultValue = PAGE_SIZE, required = false) Integer pageSize,
                                                                  @RequestParam(name = "sortOrder",defaultValue = SORT_ORDER, required = false) String sortOrder,
                                                                  @RequestParam(name = "sortBy",defaultValue = SORT_PRODUCT_BY, required = false) String sortBy,
                                                                  @PathVariable String keyword){
        ProductResponse response = productService.searchProductByKeyword(pageNumber,pageSize,sortOrder,sortBy,keyword);
        return new ResponseEntity<>(response,FOUND);
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@Valid @PathVariable Long productId,@RequestBody ProductDTO productDTO){
        ProductDTO response = productService.updateProduct(productId,productDTO);

        return new ResponseEntity<>(response,OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
        ProductDTO response = productService.deleteProduct(productId);
        return new ResponseEntity<>(response,OK);
    }

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId, @RequestParam("image")MultipartFile image) throws IOException {
        ProductDTO productDTO = productService.updateProductImage(productId,image);
        return new ResponseEntity<>(productDTO,OK);
    }
}
