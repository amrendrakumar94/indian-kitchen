package com.example.kitchen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartResponseDto {
    private CartItemDto cartItem;
    private CartSummaryDto cartSummary;
}
