package com.example.kitchen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddOnDto {
    private Integer id;
    private String name;
    private Integer price;
}
