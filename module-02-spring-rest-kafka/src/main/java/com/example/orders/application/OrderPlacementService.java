package com.example.orders.application;

import com.example.orders.api.CreateOrderRequest;
import com.example.orders.api.OrderResponse;
import com.example.orders.event.OrderPlacedEvent;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class OrderPlacementService {

    private final PricingStrategy pricing;
    private final OrderEventPublisher publisher;
    private final Clock clock;
    private final Map<String, OrderResponse> store = new ConcurrentHashMap<>();

    public OrderPlacementService(PricingStrategyFactory factory, OrderEventPublisher publisher, Clock clock) {
        this.pricing = factory.current();
        this.publisher = publisher;
        this.clock = clock;
    }

    public OrderResponse placeOrder(CreateOrderRequest request) {
        BigDecimal total = pricing.price(request.items());
        String orderId = UUID.randomUUID().toString();
        OrderResponse response = new OrderResponse(orderId, request.customerId(), total, "PLACED");
        store.put(orderId, response);

        // Publish after storing. A real system would use the transactional outbox pattern here.
        publisher.publishPlaced(new OrderPlacedEvent(orderId, request.customerId(), total, Instant.now(clock)));
        return response;
    }

    public Optional<OrderResponse> findOrder(String orderId) {
        return Optional.ofNullable(store.get(orderId));
    }
}
