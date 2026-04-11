package com.example.kitchen.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Data
@ConfigurationProperties(prefix = "kitchen.cache")
public class KitchenCacheProperties {
    private Duration menuTtl = Duration.ofMinutes(5);
    private Duration menuSearchTtl = Duration.ofMinutes(2);
    private Duration orderTtl = Duration.ofMinutes(3);
    private Duration orderListTtl = Duration.ofMinutes(1);
}
