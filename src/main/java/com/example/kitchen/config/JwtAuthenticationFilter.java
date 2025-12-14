package com.example.kitchen.config;

import com.example.kitchen.constants.CommonConstants;
import com.example.kitchen.service.JwtService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.sf.json.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private HandlerExceptionResolver exceptionResolver;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            final String userName = jwtService.extractUsername(jwt);
            if (userName == null) {
                sendUserNotFound(response);
                return;
            }
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            exceptionResolver.resolveException(request, response, null, exception);
        }
    }

    private void sendUserNotFound(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", CommonConstants.USER_NOT_FOUND);
        jsonObject.put("status", false);
        response.getWriter().write(jsonObject.toString());
    }
}
