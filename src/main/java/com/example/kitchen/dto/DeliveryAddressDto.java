package com.example.kitchen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAddressDto {
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String phone;
}
