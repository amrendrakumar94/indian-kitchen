package com.example.kitchen.modal;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "cart_details")
public class CartDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "user_id")
    int userId;

    @Column(name = "count")
    int count;

    @Column(name = "dish_id")
    int dishId;
}
