package com.example.kitchen.controller;

import com.example.kitchen.dto.AuthRequestDto;
import com.example.kitchen.dto.LoginRequestDto;
import com.example.kitchen.dto.ResponseDto;
import com.example.kitchen.dto.SignupRequestDto;
import com.example.kitchen.service.AuthenticationService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth/")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("authenticate/user")
    public ResponseEntity<ResponseDto> auth(@RequestBody AuthRequestDto request) {

        if (request instanceof LoginRequestDto loginRequestDto) {
            return authenticationService.handleLogin(loginRequestDto);
        }

        if (request instanceof SignupRequestDto signupRequestDto) {
            return authenticationService.handleSignup(signupRequestDto);
        }


        return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }
}
