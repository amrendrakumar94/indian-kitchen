package com.example.kitchen.infrastructure.messaging.producer;

import com.example.kitchen.config.properties.KitchenKafkaProperties;
import com.example.kitchen.infrastructure.messaging.event.OrderLifecycleEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventSender {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KitchenKafkaProperties kafkaProperties;

    @Retryable(
            include = Exception.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2.0))
    public void sendOrderLifecycleEvent(OrderLifecycleEvent event) throws Exception {
        kafkaTemplate.send(kafkaProperties.getTopics().getOrderLifecycle(), event.getAggregateId(), event)
                .get(5, TimeUnit.SECONDS);
        log.info("Published order event. eventType: {}, orderId: {}",
                event.getEventType(), event.getAggregateId());
    }

    @Recover
    public void recover(Exception ex, OrderLifecycleEvent event) {
        log.error("Exhausted retries while publishing order event. eventType: {}, orderId: {}",
                event.getEventType(), event.getAggregateId(), ex);
    }
}
