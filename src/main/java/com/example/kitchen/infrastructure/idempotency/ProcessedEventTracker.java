package com.example.kitchen.infrastructure.idempotency;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class ProcessedEventTracker {

    private static final Duration TTL = Duration.ofHours(24);

    private final StringRedisTemplate stringRedisTemplate;

    public boolean isProcessed(String eventId) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(buildKey(eventId)));
    }

    public void markProcessed(String eventId) {
        stringRedisTemplate.opsForValue().set(buildKey(eventId), "1", TTL);
    }

    private String buildKey(String eventId) {
        return "event:processed:" + eventId;
    }
}
