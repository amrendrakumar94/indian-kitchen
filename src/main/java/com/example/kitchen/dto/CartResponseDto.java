package com.example.kitchen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDto {
    private List<CartItemDto> items = new ArrayList<>();
    private CartSummaryDto summary = new CartSummaryDto();
}
