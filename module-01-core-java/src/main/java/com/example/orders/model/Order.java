package com.example.orders.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Immutable order snapshot. State changes produce a new Order via {@link #withStatus(OrderStatus)}.
 */
public record Order(String id, String customerId, List<OrderItem> items, OrderStatus status) {

    public Order {
        items = List.copyOf(items); // defensive, unmodifiable copy
        if (items.isEmpty()) {
            throw new IllegalArgumentException("order must contain at least one item");
        }
    }

    public BigDecimal total() {
        return items.stream()
                .map(OrderItem::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Order withStatus(OrderStatus newStatus) {
        return new Order(id, customerId, items, newStatus);
    }
}
