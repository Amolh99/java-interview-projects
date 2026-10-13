package com.example.orders.application;

import com.example.orders.api.CreateOrderRequest;
import com.example.orders.event.OrderPlacedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderPlacementServiceTest {

    @Mock OrderEventPublisher publisher;

    @Test
    void placesOrderAndPublishesEventWithComputedTotal() {
        Clock fixed = Clock.fixed(Instant.parse("2026-01-01T00:00:00Z"), ZoneOffset.UTC);
        PricingStrategyFactory factory = new PricingStrategyFactory(
                Map.of("standardPricing", new StandardPricing()), "standardPricing");
        OrderPlacementService service = new OrderPlacementService(factory, publisher, fixed);

        var request = new CreateOrderRequest("alice",
                List.of(new CreateOrderRequest.Line("SKU", 2, new BigDecimal("7.50"))));
        var response = service.placeOrder(request);

        assertEquals(0, new BigDecimal("15.00").compareTo(response.total()));
        assertEquals("PLACED", response.status());
        assertTrue(service.findOrder(response.orderId()).isPresent());

        ArgumentCaptor<OrderPlacedEvent> captor = ArgumentCaptor.forClass(OrderPlacedEvent.class);
        verify(publisher).publishPlaced(captor.capture());
        assertEquals(response.orderId(), captor.getValue().orderId());
        assertEquals(fixed.instant(), captor.getValue().placedAt());
    }

    @Test
    void unknownStrategyNameFailsFast() {
        assertThrows(IllegalStateException.class, () ->
                new PricingStrategyFactory(Map.of("standardPricing", new StandardPricing()), "nope"));
    }
}
