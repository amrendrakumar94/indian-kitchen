package com.example.kitchen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchResponseDto {
    private List<ProductResponseDto> products;
    private PaginationDto pagination;
    private AppliedFiltersDto filters;
}
