package com.example.kitchen.dto;

import com.example.kitchen.enums.DietaryType;
import com.example.kitchen.enums.SpiceLevel;
import lombok.Data;

import java.util.List;

@Data
public class FilterDto {
    String            category;
    Integer           minPrice;
    Integer           maxPrice;
    SpiceLevel        spiceLevel;
    List<DietaryType> dietary;
    String            search;
    String            cuisine;
    String            mealType;
}
