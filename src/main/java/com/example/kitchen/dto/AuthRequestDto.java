package com.example.kitchen.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "authType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = LoginRequestDto.class, name = "login"),
        @JsonSubTypes.Type(value = SignupRequestDto.class, name = "signup")
})
public interface AuthRequestDto {

}
