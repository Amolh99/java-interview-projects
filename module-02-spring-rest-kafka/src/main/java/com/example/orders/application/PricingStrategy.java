package com.example.orders.application;

import com.example.orders.api.CreateOrderRequest;

import java.math.BigDecimal;
import java.util.List;

/** Strategy pattern: pricing rules are interchangeable and selected at runtime. */
public interface PricingStrategy {

    BigDecimal price(List<CreateOrderRequest.Line> lines);
}
