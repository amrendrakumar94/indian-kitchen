package com.example.kitchen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private String orderId;
    private String orderNumber;
    private LocalDateTime orderDate;
    private LocalDateTime estimatedDelivery;
    private String status;
    private List<OrderItemDto> items = new ArrayList<>();
    private OrderSummaryDto summary = new OrderSummaryDto();
}
