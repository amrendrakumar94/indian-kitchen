package com.example.kitchen.infrastructure.ratelimit;

import com.example.kitchen.config.properties.RateLimitProperties;
import com.example.kitchen.constants.CommonConstants;
import com.example.kitchen.modal.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {

    private final RedisRateLimiterService rateLimiterService;
    private final RateLimitProperties rateLimitProperties;
    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        RateLimitPolicy policy = resolvePolicy(request);
        if (policy == null) {
            return true;
        }

        RateLimitDecision decision = rateLimiterService.allow(policy.getKey(), policy.getCapacity(), policy.getRefillPerSecond());
        response.setHeader("X-RateLimit-Remaining", String.valueOf(decision.getRemainingTokens()));

        if (decision.isAllowed()) {
            return true;
        }

        long retryAfterSeconds = Math.max(1, (long) Math.ceil(decision.getRetryAfterMillis() / 1000.0));
        response.setStatus(429);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setHeader("Retry-After", String.valueOf(retryAfterSeconds));

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", CommonConstants.FAILURE);
        body.put("message", "Rate limit exceeded");
        body.put("data", "Retry after " + retryAfterSeconds + " seconds");
        objectMapper.writeValue(response.getWriter(), body);
        return false;
    }

    private RateLimitPolicy resolvePolicy(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();

        if (HttpMethod.POST.matches(method) && "/api/auth/authenticate/user".equals(uri)) {
            return new RateLimitPolicy("rl:ip:auth:" + extractClientIp(request), rateLimitProperties.getAuthPerMinute(), perSecond(rateLimitProperties.getAuthPerMinute()));
        }

        if (HttpMethod.POST.matches(method) && "/api/orders/place".equals(uri)) {
            return new RateLimitPolicy("rl:user:order:" + resolvePrincipalKey(request), rateLimitProperties.getOrderPerMinute(), perSecond(rateLimitProperties.getOrderPerMinute()));
        }

        if ((HttpMethod.POST.matches(method) || HttpMethod.PUT.matches(method) || HttpMethod.DELETE.matches(method))
                && uri.startsWith("/api/cart")) {
            return new RateLimitPolicy("rl:user:cart:" + resolvePrincipalKey(request), rateLimitProperties.getCartPerMinute(), perSecond(rateLimitProperties.getCartPerMinute()));
        }

        if (HttpMethod.POST.matches(method)
                && ("/api/products/search".equals(uri) || "/api/food-items/search".equals(uri))) {
            return new RateLimitPolicy("rl:ip:search:" + extractClientIp(request), rateLimitProperties.getSearchPerMinute(), perSecond(rateLimitProperties.getSearchPerMinute()));
        }

        return null;
    }

    private double perSecond(int perMinute) {
        return Math.max(0.01d, perMinute / 60.0d);
    }

    private String resolvePrincipalKey(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User user) {
            return String.valueOf(user.getId());
        }
        return extractClientIp(request);
    }

    private String extractClientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    @lombok.Value
    private static class RateLimitPolicy {
        String key;
        long capacity;
        double refillPerSecond;
    }
}
