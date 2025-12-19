package com.example.kitchen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    private Integer id;
    private Integer productId;
    private String productName;
    private String productDescription;
    private String productImage;
    private String category;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer discount;
    private Integer quantity;
    private BigDecimal subtotal;
    private Boolean inStock;
    private Integer maxQuantity = 10;
}
