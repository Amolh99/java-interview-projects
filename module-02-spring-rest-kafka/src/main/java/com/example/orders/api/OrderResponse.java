package com.example.orders.api;

import java.math.BigDecimal;

public record OrderResponse(String orderId, String customerId, BigDecimal total, String status) {}
