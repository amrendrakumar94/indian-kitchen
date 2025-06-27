package com.example.kitchen.dto;

import com.example.kitchen.modal.DishDetails;
import lombok.Data;

@Data
public class CartDetailDto {
    int count;
    DishDetails dishDetail;
}
