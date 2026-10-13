package com.example.rabbit;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Idempotent consumer. The processed set stands in for a database table.
 * Invalid messages are rejected without requeue, so they go to the dead-letter queue.
 */
@Component
public class InventoryListener {

    private final Set<String> processed = ConcurrentHashMap.newKeySet();

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void onOrderPlaced(OrderPlacedMessage message) {
        handle(message);
    }

    public boolean handle(OrderPlacedMessage message) {
        if (message.orderId() == null || message.orderId().isBlank()) {
            throw new AmqpRejectAndDontRequeueException("missing orderId");
        }
        return processed.add(message.orderId()); // false means duplicate, ignored
    }
}
