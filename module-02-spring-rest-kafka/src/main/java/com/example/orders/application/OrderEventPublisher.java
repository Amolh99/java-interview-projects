package com.example.orders.application;

import com.example.orders.event.OrderPlacedEvent;

/** Port: the application depends on this interface, not on Kafka (dependency inversion). */
public interface OrderEventPublisher {

    void publishPlaced(OrderPlacedEvent event);
}
