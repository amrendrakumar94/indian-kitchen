package com.example.kitchen.dto;

import com.example.kitchen.constants.CommonConstants;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
public class ResponseDto {
    private String message;
    private boolean status;
    private Object data;

    public static ResponseEntity<ResponseDto> successResponse(Object data) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(true);
        responseDto.setData(data);
        responseDto.setMessage("Success");
        return ResponseEntity.ok(responseDto);
    }

    public static ResponseEntity<ResponseDto> successResponse(Object data, String message) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(true);
        responseDto.setData(data);
        responseDto.setMessage(message);
        return ResponseEntity.ok(responseDto);
    }

    public static ResponseEntity<ResponseDto> errorResponse(Exception e) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage(CommonConstants.SOMETHING_WENT_WRONG);
        responseDto.setData(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
    }

    public static ResponseEntity<ResponseDto> errorResponse(HttpStatus httpStatus, String message) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage(message);
        return ResponseEntity.status(httpStatus).body(responseDto);
    }

    public static ResponseEntity<ResponseDto> errorResponse(String data, String message) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage(message);
        responseDto.setStatus(false);
        responseDto.setData(data);
        return ResponseEntity.ok(responseDto);
    }
}
