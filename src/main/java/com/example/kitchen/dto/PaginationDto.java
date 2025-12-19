package com.example.kitchen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationDto {
    private Integer currentPage;
    private Integer pageSize;
    private Integer totalPages;
    private Long totalItems;
    private Boolean hasNextPage;
    private Boolean hasPreviousPage;
}
