package com.example.kitchen.infrastructure.ratelimit;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RedisRateLimiterService {

    private final StringRedisTemplate stringRedisTemplate;

    private final DefaultRedisScript<List> tokenBucketScript = buildScript();

    public RateLimitDecision allow(String key, long capacity, double refillTokensPerSecond) {
        List<?> result = stringRedisTemplate.execute(
                tokenBucketScript,
                Collections.singletonList(key),
                String.valueOf(capacity),
                String.valueOf(refillTokensPerSecond),
                String.valueOf(Instant.now().toEpochMilli()),
                "1");

        if (result == null || result.size() < 3) {
            return new RateLimitDecision(true, capacity - 1, 0);
        }

        long allowed = ((Number) result.get(0)).longValue();
        long remaining = ((Number) result.get(1)).longValue();
        long retryAfterMillis = ((Number) result.get(2)).longValue();
        return new RateLimitDecision(allowed == 1, remaining, retryAfterMillis);
    }

    private DefaultRedisScript<List> buildScript() {
        DefaultRedisScript<List> script = new DefaultRedisScript<>();
        script.setLocation(new ClassPathResource("lua/token_bucket.lua"));
        script.setResultType(List.class);
        return script;
    }
}
