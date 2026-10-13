package com.example.rabbit;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class InventoryListenerTest {

    private final InventoryListener listener = new InventoryListener();

    @Test
    void firstDeliveryIsProcessed() {
        assertTrue(listener.handle(new OrderPlacedMessage("o-1", "alice", BigDecimal.TEN)));
    }

    @Test
    void redeliveryIsIgnored() {
        var msg = new OrderPlacedMessage("o-2", "alice", BigDecimal.TEN);
        assertTrue(listener.handle(msg));
        assertFalse(listener.handle(msg));
    }

    @Test
    void invalidMessageIsRejectedWithoutRequeue() {
        assertThrows(AmqpRejectAndDontRequeueException.class,
                () -> listener.handle(new OrderPlacedMessage(" ", "alice", BigDecimal.TEN)));
    }
}
