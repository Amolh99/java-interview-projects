package com.example.orders.model;

import java.math.BigDecimal;

/** Sealed hierarchy: the compiler knows every permitted subtype, so switches can be exhaustive. */
public sealed interface OrderEvent permits OrderEvent.Placed, OrderEvent.Paid, OrderEvent.Shipped, OrderEvent.Cancelled {

    String orderId();

    record Placed(String orderId, String customerId) implements OrderEvent {}

    record Paid(String orderId, BigDecimal amount) implements OrderEvent {}

    record Shipped(String orderId, String trackingId) implements OrderEvent {}

    record Cancelled(String orderId, String reason) implements OrderEvent {}
}
