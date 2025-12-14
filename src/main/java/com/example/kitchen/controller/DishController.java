package com.example.kitchen.controller;

import com.example.kitchen.dto.DishDetailsDto;
import com.example.kitchen.modal.DishDetails;
import com.example.kitchen.service.DishService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/")
public class DishController {
    
    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping("/get-all-products/{userId}/{cuisineType}")
    public DishDetailsDto getAllDishes(@PathVariable int userId, @PathVariable String cuisineType) {
        return dishService.getAllDishes(userId, cuisineType);
    }


    @GetMapping("/get-dish-details-by-dish-ids/{dishIds}")
    public List<DishDetails> getDishDetailsByDishIds(@PathVariable String dishIds) {
        return dishService.getDishDetailsByDishIds(dishIds);
    }
}
