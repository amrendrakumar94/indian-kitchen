package com.example.kitchen.dto;

import lombok.Data;

@Data
public class LoginRequestDto implements AuthRequestDto {
    private String phoneNo;
    private String password;
}
