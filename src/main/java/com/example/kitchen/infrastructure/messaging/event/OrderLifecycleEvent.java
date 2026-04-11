package com.example.kitchen.infrastructure.messaging.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderLifecycleEvent {
    private String eventId;
    private String eventType;
    private String aggregateType;
    private String aggregateId;
    private LocalDateTime occurredAt;
    private Integer userId;
    private String orderNumber;
    private String orderStatus;
    private BigDecimal total;
}
