package com.example.kitchen.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderHistoryResponseDto {
    private List<OrderHistoryDto> orders = new ArrayList<>();
    private PaginationDto pagination = new PaginationDto();
}
