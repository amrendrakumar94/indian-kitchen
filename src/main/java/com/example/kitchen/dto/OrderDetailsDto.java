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
public class OrderDetailsDto {
    private String orderId;
    private String orderNumber;
    private LocalDateTime orderDate;
    private String status;
    private List<OrderStatusHistoryDto> statusHistory = new ArrayList<>();
    private List<OrderItemDto> items = new ArrayList<>();
    private OrderSummaryDto summary = new OrderSummaryDto();
    private DeliveryAddressDto deliveryAddress;
    private String paymentMethod;
    private String paymentStatus;
    private String specialInstructions;
}
