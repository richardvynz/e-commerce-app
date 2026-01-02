package com.richardvinz.eCommerce_App.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private List<ProductDTO> contents;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElement;
    private Integer totalPage;
    private boolean isLast;
}
