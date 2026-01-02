package com.richardvinz.eCommerce_App.product.service;

import com.richardvinz.eCommerce_App.category.exception.APIException;
import com.richardvinz.eCommerce_App.category.exception.ResourceNotFoundException;
import com.richardvinz.eCommerce_App.category.model.Category;
import com.richardvinz.eCommerce_App.category.repository.CategoryRepository;
import com.richardvinz.eCommerce_App.product.dto.ProductDTO;
import com.richardvinz.eCommerce_App.product.dto.ProductResponse;
import com.richardvinz.eCommerce_App.product.model.Product;
import com.richardvinz.eCommerce_App.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final FileService fileService;

    @Value("${product.image}")
    private String path;

    @Override
    public ProductDTO createProduct(ProductDTO productDTO, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                ()-> new ResourceNotFoundException("Category","categoryId",categoryId)
        );

        boolean isProductNotPresent = true;

        List<Product> products = category.getProducts();

        for(Product value: products){
            if(value.getProductName().equals(productDTO.getProductName())){
                isProductNotPresent = false;
                break;
            }
        }

            if(isProductNotPresent)
        {
            Product product = modelMapper.map(productDTO, Product.class);

            product.setCategory(category);
            product.setImage("default.png");

            calculateSpecialPrice(product);

            Product savedProduct = productRepository.save(product);

            return modelMapper.map(savedProduct, ProductDTO.class);
        } else{
    throw new APIException("PRODUCT ALREADY EXIST!!");
}
    }

    private static void calculateSpecialPrice(Product product) {
        double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
        product.setSpecialPrice(specialPrice);
    }
    @Override
    public ProductResponse getProductList(Integer pageNumber, Integer pageSize,
                                          String sortOrder, String sortBy) {
        Sort sortByAndOrder =
                sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                        : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);

        Page<Product> products = productRepository.findAll(pageDetails);
        List <Product> productList = products.getContent();

        List<ProductDTO> productDTOList =
                productList.stream()
                        .map((element)
                                -> modelMapper.map(element, ProductDTO.class)).toList();
        ProductResponse response = new ProductResponse();
        response.setContents(productDTOList);
        response.setPageNumber(products.getNumber());
        response.setPageSize(products.getSize());
        response.setTotalElement(products.getTotalElements());
        response.setTotalPage(products.getTotalPages());
        response.setLast(products.isLast());

        return response;
    }

    @Override
    public ProductResponse searchProductsByCategory(Integer pageNumber, Integer pageSize,
                                                    String sortOrder, String sortBy, Long categoryId) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageDetail = PageRequest.of(pageNumber,pageSize,sortByAndOrder);

        Category category = categoryRepository.findById(categoryId).orElseThrow(
                ()-> new ResourceNotFoundException("Category","categoryId",categoryId)
        );

    Page<Product> productPage = productRepository.findByCategoryOrderByPriceAsc(category,pageDetail);

    List<Product> productList = productPage.getContent();

    List<ProductDTO> content = productList.stream().map((element) -> modelMapper.map(element, ProductDTO.class)).toList();

    ProductResponse response = new ProductResponse();
    response.setContents(content);
    response.setPageNumber(productPage.getNumber());
    response.setPageSize(productPage.getSize());
    response.setTotalElement(productPage.getTotalElements());
    response.setTotalPage(productPage.getTotalPages());
    response.setLast(productPage.isLast());

    return response;

    }
    @Override
    public ProductResponse searchProductByKeyword(Integer pageNumber, Integer pageSize, String sortOrder, String sortBy, String keyword) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);

        Page<Product> productPage = productRepository.findByProductNameLikeIgnoreCase("%" + keyword + "%",pageDetails);
        List<Product> productList = productPage.getContent();
        List<ProductDTO> content = productList.stream().map((element) -> modelMapper.map(element, ProductDTO.class)).toList();

        ProductResponse response = new ProductResponse();
        response.setContents(content);
        response.setPageNumber(productPage.getNumber());
        response.setPageSize(productPage.getSize());
        response.setTotalElement(productPage.getTotalElements());
        response.setTotalPage(productPage.getTotalPages());
        response.setLast(productPage.isLast());

        return response;
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product foundProduct = productRepository.findById(productId).orElseThrow(
                ()-> new ResourceNotFoundException("Product","productId",productId)
        );
        foundProduct.setProductName(productDTO.getProductName());
        foundProduct.setDiscount(productDTO.getDiscount());
        foundProduct.setDescription(productDTO.getDescription());
        foundProduct.setQuantity(productDTO.getQuantity());
        foundProduct.setPrice(productDTO.getPrice());
        calculateSpecialPrice(foundProduct);
        Product savedProduct = productRepository.save(foundProduct);

        return modelMapper.map(savedProduct, ProductDTO.class);

    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product foundProduct = productRepository.findById(productId).orElseThrow(
                ()-> new ResourceNotFoundException("Product","productId",productId)
        );
        productRepository.delete(foundProduct);
        return modelMapper.map(foundProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {

        /*
        * get product from db
        * upload image to server
        * get file name of uploaded image
        * updating the new file name to the product
        * save updated product
        * return dto after mapping
        * */

        Product foundProduct = productRepository.findById(productId).orElseThrow(
                ()-> new ResourceNotFoundException("Product","productId",productId)
        );
        String fileName = fileService.uploadFile(path,image);
        foundProduct.setImage(fileName);
        Product savedProduct = productRepository.save(foundProduct);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }
}
