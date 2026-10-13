package com.example.orders.messaging;

import com.example.orders.event.OrderPlacedEvent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Consumer side. Kafka delivers at-least-once, so processing must be idempotent.
 * The in-memory set stands in for a processed_events table in a real service.
 */
@Component
public class InventoryReservationListener {

    private static final Logger log = LoggerFactory.getLogger(InventoryReservationListener.class);

    private final Set<String> processed = ConcurrentHashMap.newKeySet();

    @KafkaListener(topics = "${orders.topic}")
    public void onOrderPlaced(OrderPlacedEvent event) {
        if (!processed.add(event.orderId())) {
            log.info("Duplicate event ignored for order {}", event.orderId());
            return;
        }
        log.info("Reserving stock for order {} (total {})", event.orderId(), event.total());
    }

    public boolean isProcessed(String orderId) {
        return processed.contains(orderId);
    }
}
