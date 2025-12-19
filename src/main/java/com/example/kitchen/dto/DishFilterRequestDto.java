package com.example.kitchen.dto;

import com.example.kitchen.enums.Cuisine;
import com.example.kitchen.enums.SortOrder;
import lombok.Data;

@Data
public class DishFilterRequestDto {
    int       page     = 1;
    int       pageSize = 20;
    String    sortBy;
    SortOrder sortOrder;
    FilterDto filters;
    Cuisine cuisine;
}
