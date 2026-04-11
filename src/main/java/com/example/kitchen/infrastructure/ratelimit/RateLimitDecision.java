package com.example.kitchen.infrastructure.ratelimit;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RateLimitDecision {
    private boolean allowed;
    private long remainingTokens;
    private long retryAfterMillis;
}
