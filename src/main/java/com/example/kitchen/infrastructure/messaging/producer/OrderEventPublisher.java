package com.example.kitchen.infrastructure.messaging.producer;

import com.example.kitchen.infrastructure.messaging.event.OrderLifecycleEvent;

public interface OrderEventPublisher {

    void publishAfterCommit(OrderLifecycleEvent event);
}
