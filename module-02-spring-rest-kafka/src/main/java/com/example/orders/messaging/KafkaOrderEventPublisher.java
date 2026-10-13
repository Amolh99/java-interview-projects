package com.example.orders.messaging;

import com.example.orders.application.OrderEventPublisher;
import com.example.orders.event.OrderPlacedEvent;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/** Adapter: implements the application port using Kafka. */
@Component
public class KafkaOrderEventPublisher implements OrderEventPublisher {

    private final KafkaTemplate<String, OrderPlacedEvent> kafka;
    private final String topic;

    public KafkaOrderEventPublisher(KafkaTemplate<String, OrderPlacedEvent> kafka,
                                    @Value("${orders.topic}") String topic) {
        this.kafka = kafka;
        this.topic = topic;
    }

    @Override
    public void publishPlaced(OrderPlacedEvent event) {
        // Keyed by orderId: all events for one order land on the same partition, so they stay ordered.
        kafka.send(topic, event.orderId(), event);
    }
}
