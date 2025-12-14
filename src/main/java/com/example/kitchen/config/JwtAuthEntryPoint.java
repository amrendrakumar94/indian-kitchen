package com.example.kitchen.config;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.example.kitchen.constants.CommonConstants;

import net.sf.json.JSONObject;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        json.put("status", CommonConstants.FAILURE);
        json.put("error", ex.getMessage());
        json.put("message", CommonConstants.USER_NOT_FOUND);
        response.getWriter().write(json.toString());
    }
}
