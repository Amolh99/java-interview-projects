package com.example.orders.model;

import java.math.BigDecimal;
import java.util.List;

/** Sealed hierarchy: the compiler knows every permitted subtype, so switches can be exhaustive. */
public sealed interface OrderEvent permits OrderEvent.Placed, OrderEvent.Paid, OrderEvent.Shipped, OrderEvent.Cancelled {

    String orderId();

    record Placed(String orderId, String customerId, List<OrderItem> items) implements OrderEvent {}

    record Paid(String orderId, BigDecimal amount) implements OrderEvent {}

    record Shipped(String orderId, String trackingId) implements OrderEvent {}

    record Cancelled(String orderId, String reason) implements OrderEvent {}
}
