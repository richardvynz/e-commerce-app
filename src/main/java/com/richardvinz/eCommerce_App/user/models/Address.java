package com.richardvinz.eCommerce_App.user.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 5, message = "street must be at least 5 characters")
    private String street;

    public Address(String street, String buildingName, String cityName, String stateName, String zipCode, String country) {
        this.street = street;
        this.buildingName = buildingName;
        this.cityName = cityName;
        this.stateName = stateName;
        this.zipCode = zipCode;
        this.country = country;
    }

    @NotBlank
    @Size(min = 5, message = "building Name must be at least 5 characters")
    private String buildingName;
    @NotBlank
    @Size(min = 5, message = "city Name must be at least 5 characters")
    private String cityName;
    @NotBlank
    @Size(min = 3, message = "state name must be at least 3 characters")
    private String stateName;
    @NotBlank
    @Size(min = 5, message = "zipCode must be at least 5 characters")
    private String zipCode;
    @NotBlank
    @Size(min = 5, message = "Country must be at least 5 characters")
    private String country;

    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    private List<User> users = new ArrayList<>();

}
