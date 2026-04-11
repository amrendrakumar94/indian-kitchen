package com.example.kitchen.infrastructure.idempotency;

import com.example.kitchen.dto.OrderResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderIdempotencyRecord {
    private String requestHash;
    private OrderResponseDto response;
}
