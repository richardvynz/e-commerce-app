package com.richardvinz.eCommerce_App.category.payload.response;

import com.richardvinz.eCommerce_App.category.payload.request.CategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
    List<CategoryDTO> content;
}
