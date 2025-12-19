package com.example.kitchen.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponseDto {
    private Integer id;
    private String name;
    private String description;
    private Integer price;
    private Integer originalPrice;
    private Integer discount;
    private String category;
    private String image;
    private List<String> images;
    private Double rating;
    private Integer reviewCount;
    private Boolean inStock;
    private String servingSize;
    private String spiceLevel;
    private List<String> dietary;
    private String cuisine;
    private String preparationTime;
    private Integer calories;
    private List<String> ingredients;
    private List<String> allergens;
    private Boolean customizable;
    private List<AddOnDto> addOns;
    private List<String> tags;
    private String brand;
    private LocalDateTime createdAt;
    private String mealType;
}
