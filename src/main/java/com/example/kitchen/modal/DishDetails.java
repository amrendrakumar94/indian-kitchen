package com.example.kitchen.modal;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "dish_details")
public class DishDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "price")
    int price;

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
}
