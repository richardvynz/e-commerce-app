package com.richardvinz.eCommerce_App.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long productId;
    @NotBlank
    @Size(min = 3, message = "product name must have at least 3 characters")
    private String productName;

    @NotBlank
    @Size(min = 6, message = "product description must have at least 6 characters")
    private String description;
    private String image;
    private Integer quantity;
    private double price;
    private double specialPrice;
    private double discount;

}
