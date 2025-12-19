package com.example.kitchen.dao;

import com.example.kitchen.dto.FilterDto;
import com.example.kitchen.modal.DishDetails;

import java.util.List;

public interface DishDao {
    List<DishDetails> getDishDetailsList(String cuisineType);

    List<DishDetails> getAllDishes();

    DishDetails getDishById(int dishId);

    void save(DishDetails dishDetails);

    List<DishDetails> getDishDetailsByDishIds(String dishIds);

    List<DishDetails> searchProducts(FilterDto filters, String sortBy, String sortOrder, int page, int pageSize);

    Long countProducts(FilterDto filters);
}
