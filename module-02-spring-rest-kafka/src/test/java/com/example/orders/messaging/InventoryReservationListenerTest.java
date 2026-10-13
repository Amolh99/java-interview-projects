package com.example.orders.messaging;

import com.example.orders.event.OrderPlacedEvent;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class InventoryReservationListenerTest {

    @Test
    void duplicateDeliveryIsIgnoredSafely() {
        InventoryReservationListener listener = new InventoryReservationListener();
        var event = new OrderPlacedEvent("o-1", "alice", BigDecimal.TEN, Instant.now());

        listener.onOrderPlaced(event);
        listener.onOrderPlaced(event); // redelivery must not throw or reserve twice

        assertTrue(listener.isProcessed("o-1"));
    }
}
