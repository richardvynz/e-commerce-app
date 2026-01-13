package com.richardvinz.eCommerce_App.product.model;

import com.richardvinz.eCommerce_App.category.model.Category;
import com.richardvinz.eCommerce_App.user.models.User;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productName;
    private String description;
    private String image;
    private Integer quantity;
    private double price;
    private double specialPrice;
    private double discount;

    @ManyToOne()
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "seller_id")
    private User user;
}
