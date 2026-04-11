package com.example.kitchen.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "kitchen.rate-limit")
public class RateLimitProperties {
    private int authPerMinute = 5;
    private int orderPerMinute = 10;
    private int cartPerMinute = 30;
    private int searchPerMinute = 60;
}
