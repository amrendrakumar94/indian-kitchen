package com.example.kitchen.infrastructure.idempotency;

import com.example.kitchen.dto.OrderResponseDto;
import com.example.kitchen.dto.PlaceOrderRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.HexFormat;

@Component
@RequiredArgsConstructor
public class OrderIdempotencyService {

    private static final Duration TTL = Duration.ofHours(24);

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public OrderResponseDto findResponse(int userId, String idempotencyKey, PlaceOrderRequest request) {
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            return null;
        }

        Object value = redisTemplate.opsForValue().get(buildKey(userId, idempotencyKey));
        if (!(value instanceof OrderIdempotencyRecord record)) {
            return null;
        }

        String requestHash = hashRequest(request);
        if (!requestHash.equals(record.getRequestHash())) {
            throw new IllegalArgumentException("Idempotency-Key was reused with a different request payload");
        }

        return record.getResponse();
    }

    public void storeResponse(int userId, String idempotencyKey, PlaceOrderRequest request, OrderResponseDto response) {
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            return;
        }

        OrderIdempotencyRecord record = new OrderIdempotencyRecord(hashRequest(request), response);
        redisTemplate.opsForValue().set(buildKey(userId, idempotencyKey), record, TTL);
    }

    private String buildKey(int userId, String idempotencyKey) {
        return "idem:order:" + userId + ":" + idempotencyKey;
    }

    private String hashRequest(PlaceOrderRequest request) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] raw = digest.digest(objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(raw);
        } catch (NoSuchAlgorithmException | JsonProcessingException e) {
            throw new IllegalStateException("Failed to hash idempotent order request", e);
        }
    }
}
