package com.example.kitchen.controller;

import java.util.List;

import com.example.kitchen.dto.DishFilterRequestDto;
import com.example.kitchen.dto.ProductSearchRequestDto;
import com.example.kitchen.dto.ProductSearchResponseDto;
import com.example.kitchen.dto.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.kitchen.dto.DishDetailsDto;
import com.example.kitchen.modal.DishDetails;
import com.example.kitchen.service.DishService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
@CrossOrigin(origins = "*")
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

    @PostMapping("products/search")
    public ResponseEntity<ResponseDto> searchProducts(@RequestBody ProductSearchRequestDto request) {
        try {
            ProductSearchResponseDto response = dishService.searchProducts(request);
            return ResponseDto.successResponse(response, "Food items fetched successfully");
        } catch (Exception e) {
            return ResponseDto.errorResponse("Error fetching products", e.getMessage());
        }
    }

    @PostMapping("food-items/search")
    public ResponseEntity<ResponseDto> searchFoodItems(@RequestBody ProductSearchRequestDto request) {
        try {
            ProductSearchResponseDto response = dishService.searchProducts(request);
            return ResponseDto.successResponse(response, "Food items fetched successfully");
        } catch (Exception e) {
            return ResponseDto.errorResponse("Error fetching food items", e.getMessage());
        }
    }
}
