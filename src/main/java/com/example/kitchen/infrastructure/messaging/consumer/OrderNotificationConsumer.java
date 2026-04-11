package com.example.kitchen.infrastructure.messaging.consumer;

import com.example.kitchen.infrastructure.idempotency.ProcessedEventTracker;
import com.example.kitchen.infrastructure.messaging.event.OrderLifecycleEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@lombok.RequiredArgsConstructor
public class OrderNotificationConsumer {

    private final ProcessedEventTracker processedEventTracker;

    @KafkaListener(
            topics = "${kitchen.kafka.topics.order-lifecycle}",
            groupId = "${spring.kafka.consumer.group-id}")
    public void onOrderLifecycleEvent(OrderLifecycleEvent event) {
        if (processedEventTracker.isProcessed(event.getEventId())) {
            log.info("Skipping duplicate order lifecycle event. eventId: {}", event.getEventId());
            return;
        }

        log.info("Notification simulation. eventType: {}, orderId: {}, userId: {}, status: {}, total: {}",
                event.getEventType(),
                event.getAggregateId(),
                event.getUserId(),
                event.getOrderStatus(),
                event.getTotal());
        processedEventTracker.markProcessed(event.getEventId());
    }
}
