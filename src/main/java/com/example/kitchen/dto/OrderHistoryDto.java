package com.example.kitchen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistoryDto {
    private String orderId;
    private String orderNumber;
    private LocalDateTime orderDate;
    private String status;
    private Integer itemCount;
    private BigDecimal total;
    private List<OrderItemDto> items = new ArrayList<>();
}
