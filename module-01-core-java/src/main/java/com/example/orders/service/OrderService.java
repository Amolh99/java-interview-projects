package com.example.orders.service;

import com.example.orders.model.Order;
import com.example.orders.model.OrderEvent;
import com.example.orders.model.OrderItem;
import com.example.orders.model.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class OrderService {

    // Thread-safe store; compute-style updates below use ConcurrentHashMap.compute for atomic transitions.
    private final Map<String, Order> orders = new ConcurrentHashMap<>();

    public Order place(String customerId, List<OrderItem> items) {
        Order order = new Order(UUID.randomUUID().toString(), customerId, items, OrderStatus.PLACED);
        orders.put(order.id(), order);
        return order;
    }

    public Optional<Order> find(String orderId) {
        return Optional.ofNullable(orders.get(orderId));
    }

    public Order pay(String orderId) {
        return transition(orderId, OrderStatus.PAID);
    }

    public Order ship(String orderId) {
        return transition(orderId, OrderStatus.SHIPPED);
    }

    public Order cancel(String orderId) {
        return transition(orderId, OrderStatus.CANCELLED);
    }

    /** Applies an event using an exhaustive pattern-matching switch over the sealed interface. */
    public Order apply(OrderEvent event) {
        return switch (event) {
            case OrderEvent.Placed e -> place(e.customerId(), List.of()).withStatus(OrderStatus.PLACED);
            case OrderEvent.Paid e -> pay(e.orderId());
            case OrderEvent.Shipped e -> ship(e.orderId());
            case OrderEvent.Cancelled e -> cancel(e.orderId());
        };
    }

    /** Revenue of non-cancelled orders per customer, sorted by customer id. */
    public Map<String, BigDecimal> revenueByCustomer() {
        return orders.values().stream()
                .filter(o -> o.status() != OrderStatus.CANCELLED)
                .collect(Collectors.groupingBy(
                        Order::customerId,
                        TreeMap::new,
                        Collectors.reducing(BigDecimal.ZERO, Order::total, BigDecimal::add)));
    }

    private Order transition(String orderId, OrderStatus target) {
        return orders.compute(orderId, (id, current) -> {
            if (current == null) {
                throw new NoSuchElementException("order not found: " + id);
            }
            if (!isAllowed(current.status(), target)) {
                throw new IllegalStateException(
                        "cannot move order " + id + " from " + current.status() + " to " + target);
            }
            return current.withStatus(target);
        });
    }

    static boolean isAllowed(OrderStatus from, OrderStatus to) {
        return switch (from) {
            case PLACED -> to == OrderStatus.PAID || to == OrderStatus.CANCELLED;
            case PAID -> to == OrderStatus.SHIPPED || to == OrderStatus.CANCELLED;
            case SHIPPED, CANCELLED -> false;
        };
    }
}
