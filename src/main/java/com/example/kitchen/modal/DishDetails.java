package com.example.kitchen.modal;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "dish_details")
public class DishDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "price")
    int price;

    @Column(name = "original_price")
    Integer originalPrice;

    @Column(name = "discount")
    Integer discount;

    @Column(name = "spiciness_level")
    int spicinessLevel;

    @Column(name = "name")
    String name;

    @Column(name = "cuisine_type")
    String cuisineType;

    @Column(name = "ingredients_list")
    String ingredientsList;

    @Column(name = "description")
    String description;

    @Column(name = "image")
    String image;

    @Column(name = "is_vegetarian")
    boolean isVegetarian;

    @Column(name = "is_recommended")
    boolean isRecommended;

    @Column(name = "rating")
    Double rating;

    @Column(name = "review_count")
    Integer reviewCount;

    @Column(name = "in_stock")
    Boolean inStock;

    @Column(name = "serving_size")
    String servingSize;

    @Column(name = "preparation_time")
    String preparationTime;

    @Column(name = "calories")
    Integer calories;

    @Column(name = "allergens")
    String allergens;

    @Column(name = "customizable")
    Boolean customizable;

    @Column(name = "tags")
    String tags;

    @Column(name = "brand")
    String brand;

    @Column(name = "meal_type")
    String mealType;

    @Column(name = "category")
    String category;

    @Column(name = "order_count")
    Integer orderCount;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;
}
