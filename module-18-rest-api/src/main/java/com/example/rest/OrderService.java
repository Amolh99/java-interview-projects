package com.example.rest;

import com.example.rest.OrderDtos.CreateOrderRequest;
import com.example.rest.OrderDtos.OrderResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;

@Service
public class OrderService {

    /** In-memory store for the demo. Module 17 shows the database version. */
    private final Map<String, OrderResponse> store = new ConcurrentHashMap<>();

    public OrderResponse create(CreateOrderRequest request) {
        BigDecimal total = request.items().stream()
                .map(i -> i.unitPrice().multiply(BigDecimal.valueOf(i.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        OrderResponse order = new OrderResponse(UUID.randomUUID().toString(),
                request.customerId(), total, "PLACED");
        store.put(order.id(), order);
        return order;
    }

    public Optional<OrderResponse> find(String id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<OrderResponse> page(int page, int size) {
        if (size < 1 || size > 100) {
            throw new IllegalArgumentException("size must be between 1 and 100");
        }
        return store.values().stream()
                .sorted((a, b) -> a.id().compareTo(b.id()))
                .skip((long) page * size)
                .limit(size)
                .toList();
    }

    /** Returns false if the order does not exist. Throws if it has already shipped. */
    public boolean cancel(String id) {
        OrderResponse current = store.get(id);
        if (current == null) {
            return false;
        }
        if ("SHIPPED".equals(current.status())) {
            throw new IllegalStateException("order " + id + " has already shipped");
        }
        store.remove(id);
        return true;
    }
}
