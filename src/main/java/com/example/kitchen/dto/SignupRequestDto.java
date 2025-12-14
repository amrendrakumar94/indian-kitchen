package com.example.kitchen.dto;

import lombok.Data;

@Data
public class SignupRequestDto implements AuthRequestDto {
    private String phoneNo;
    private String password;
    private String name;
}
