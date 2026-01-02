package com.richardvinz.eCommerce_App.category.model;

import com.richardvinz.eCommerce_App.product.model.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
    @NotBlank
    @Size(min = 5, message = "Category must not be less than 5 Characters")
    private String categoryName;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Product> products;

}
