package com.example.kitchen.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.kitchen.constants.CommonConstants;
import com.example.kitchen.service.JwtService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService         jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        final String userName;

        try {
            userName = jwtService.extractUsername(jwt);
        } catch (Exception ex) {
            throw new BadCredentialsException(CommonConstants.USER_NOT_FOUND);
        }
        if (userName == null) {
            throw new BadCredentialsException(CommonConstants.USER_NOT_FOUND);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // already authenticated → continue
        if (authentication != null) {
            filterChain.doFilter(request, response);
            return;
        }
        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(userName);
        } catch (Exception ex) {
            throw new BadCredentialsException(CommonConstants.USER_NOT_FOUND);
        }

        if (!jwtService.isTokenValid(jwt, userDetails)) {
            throw new BadCredentialsException(CommonConstants.USER_NOT_FOUND);
        }
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }
}
