package com.example.kitchen.dto;

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
    private long   tokenExpire;

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

    public static ResponseEntity<ResponseDto> successResponse(String token, String message, long expirationTime) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(CommonConstants.SUCCESS);
        responseDto.setToken(token);
        responseDto.setTokenExpire(expirationTime);
        responseDto.setMessage(message);
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
