package com.example.kitchen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppliedFiltersDto {
    private Map<String, Object> appliedFilters;
    private List<String> availableCategories;
}
