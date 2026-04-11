package com.example.kitchen.infrastructure.messaging.producer;

import com.example.kitchen.config.properties.KitchenKafkaProperties;
import com.example.kitchen.infrastructure.messaging.event.OrderLifecycleEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaOrderEventPublisher implements OrderEventPublisher {

    private final KafkaEventSender kafkaEventSender;

    @Override
    public void publishAfterCommit(OrderLifecycleEvent event) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    publish(event);
                }
            });
            return;
        }

        publish(event);
    }

    private void publish(OrderLifecycleEvent event) {
        try {
            kafkaEventSender.sendOrderLifecycleEvent(event);
        } catch (Exception ex) {
            log.error("Failed to publish order event after retries. eventType: {}, orderId: {}",
                    event.getEventType(), event.getAggregateId(), ex);
        }
    }
}
