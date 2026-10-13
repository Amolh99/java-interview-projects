package com.example.orders.event;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderPlacedEvent(String orderId, String customerId, BigDecimal total, Instant placedAt) {}
