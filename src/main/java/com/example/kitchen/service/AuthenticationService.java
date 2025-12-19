package com.example.kitchen.service;

import com.example.kitchen.constants.CommonConstants;
import com.example.kitchen.dao.UserDao;
import com.example.kitchen.dto.LoginRequestDto;
import com.example.kitchen.dto.ResponseDto;
import com.example.kitchen.dto.SignupRequestDto;
import com.example.kitchen.modal.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder       passwordEncoder;
    private final JwtService            jwtService;
    private final UserDao               userDao;

    public ResponseEntity<ResponseDto> handleSignup(SignupRequestDto signupRequestDto) {
        try {
            if (userDao.getUserByPhone(signupRequestDto.getPhoneNo()).isPresent()) {
                return ResponseDto.errorResponse(null, CommonConstants.USER_ALREADY_EXISTS);
            }
            User user = new User();
            user.setPhoneNo(signupRequestDto.getPhoneNo());
            user.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));
            user.setName(signupRequestDto.getName());
            userDao.saveOrUpdateUser(user);
            return ResponseDto.successResponse(user, CommonConstants.SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseDto.errorResponse();
    }

    public ResponseEntity<ResponseDto> handleLogin(LoginRequestDto loginRequestDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getPhoneNo(), loginRequestDto.getPassword()));
            User authenticatedUser = userDao.getUserByPhone(loginRequestDto.getPhoneNo()).orElseThrow();
            String jwtToken = jwtService.generateToken(authenticatedUser);
            long expirationTime = jwtService.getExpirationTime();
            return ResponseDto.successResponse(jwtToken, authenticatedUser, CommonConstants.LOGED_IN, expirationTime);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseDto.errorResponse();
    }
}
