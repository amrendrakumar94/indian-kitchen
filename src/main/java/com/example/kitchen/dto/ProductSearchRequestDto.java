package com.example.kitchen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchRequestDto {
    private Integer page = 1;
    private Integer pageSize = 20;
    private String sortBy = "popular";
    private String sortOrder = "desc";
    private FilterDto filters;
    private Integer userId;
}
