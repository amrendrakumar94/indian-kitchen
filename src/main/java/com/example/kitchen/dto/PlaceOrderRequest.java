package com.example.kitchen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderRequest {
    private DeliveryAddressDto deliveryAddress;
    private String paymentMethod;
    private String specialInstructions;
}
