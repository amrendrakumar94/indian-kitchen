package com.example.kitchen.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kitchen.dto.DishDetailsDto;
import com.example.kitchen.modal.DishDetails;
import com.example.kitchen.service.DishService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/noauth/api/")
public class DishController {

    private final DishService dishService;

    @GetMapping("get-all-products/{userId}/{cuisineType}")
    public DishDetailsDto getAllDishes(@PathVariable int userId, @PathVariable String cuisineType) {
        return dishService.getAllDishes(userId, cuisineType);
    }

    @GetMapping("get-dish-details-by-dish-ids/{dishIds}")
    public List<DishDetails> getDishDetailsByDishIds(@PathVariable String dishIds) {
        return dishService.getDishDetailsByDishIds(dishIds);
    }
}
