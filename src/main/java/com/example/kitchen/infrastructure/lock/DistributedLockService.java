package com.example.kitchen.infrastructure.lock;

import java.time.Duration;

public interface DistributedLockService {

    String tryAcquire(String key, Duration ttl);

    void release(String key, String token);
}
