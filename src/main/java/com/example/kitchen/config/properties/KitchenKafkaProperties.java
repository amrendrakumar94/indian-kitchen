package com.example.kitchen.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "kitchen.kafka")
public class KitchenKafkaProperties {
    private Topics topics = new Topics();

    @Data
    public static class Topics {
        private String orderLifecycle = "kitchen.order.lifecycle.v1";
        private String notification = "kitchen.notification.events.v1";
        private String deadLetter = "kitchen.notification.events.v1.dlq";
    }
}
