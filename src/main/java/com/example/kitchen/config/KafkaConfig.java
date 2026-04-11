package com.example.kitchen.config;

import com.example.kitchen.config.properties.KitchenKafkaProperties;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic orderLifecycleTopic(KitchenKafkaProperties properties) {
        return TopicBuilder.name(properties.getTopics().getOrderLifecycle())
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic notificationTopic(KitchenKafkaProperties properties) {
        return TopicBuilder.name(properties.getTopics().getNotification())
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic deadLetterTopic(KitchenKafkaProperties properties) {
        return TopicBuilder.name(properties.getTopics().getDeadLetter())
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public CommonErrorHandler kafkaErrorHandler(KafkaTemplate<Object, Object> kafkaTemplate,
                                                KitchenKafkaProperties properties) {
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(
                kafkaTemplate,
                (record, ex) -> new TopicPartition(properties.getTopics().getDeadLetter(), record.partition()));

        return new DefaultErrorHandler(recoverer, new FixedBackOff(2000L, 3L));
    }
}
