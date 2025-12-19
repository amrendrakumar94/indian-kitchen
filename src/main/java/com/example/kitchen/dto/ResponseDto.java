package com.example.kitchen.dto;

import com.example.kitchen.modal.User;
import org.springframework.http.ResponseEntity;

import com.example.kitchen.constants.CommonConstants;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto {
    private String message;
    private String status;
    private Object data;
    private String token;
    private Long   tokenExpire;

    @Data
    static class UserDetailsDto {
        private String name;
        private String email;
        private String phone;
        private String address;
    }

    public static ResponseEntity<ResponseDto> successResponse(Object data) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(CommonConstants.SUCCESS);
        responseDto.setData(data);
        responseDto.setMessage(CommonConstants.SUCCESS);
        return ResponseEntity.ok(responseDto);
    }

    public static ResponseEntity<ResponseDto> successResponse(Object data, String message) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(CommonConstants.SUCCESS);
        responseDto.setData(data);
        responseDto.setMessage(message);
        return ResponseEntity.ok(responseDto);
    }

    public static ResponseEntity<ResponseDto> successResponse(String token, User user, String message, long expirationTime) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(CommonConstants.SUCCESS);
        responseDto.setToken(token);
        responseDto.setTokenExpire(expirationTime);
        responseDto.setMessage(message);
        UserDetailsDto data = new UserDetailsDto();
        data.setName(user.getName());
        data.setEmail(user.getEmail());
        data.setAddress("");
        data.setPhone(user.getPhoneNo());
        responseDto.setData(data);
        return ResponseEntity.ok(responseDto);
    }

    public static ResponseEntity<ResponseDto> errorResponse(String data, String message) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage(message);
        responseDto.setStatus(CommonConstants.FAILURE);
        responseDto.setData(data);
        return ResponseEntity.ok(responseDto);
    }

    public static ResponseEntity<ResponseDto> errorResponse() {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage(CommonConstants.SOMETHING_WENT_WRONG);
        responseDto.setStatus(CommonConstants.FAILURE);
        return ResponseEntity.ok(responseDto);
    }
}
