package com.example.orders.application;

import com.example.orders.api.CreateOrderRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BulkDiscountPricingTest {

    private final BulkDiscountPricing pricing = new BulkDiscountPricing(new StandardPricing());

    @Test
    void noDiscountBelowThreshold() {
        var lines = List.of(new CreateOrderRequest.Line("A", 9, new BigDecimal("10.00")));
        assertEquals(0, new BigDecimal("90.00").compareTo(pricing.price(lines)));
    }

    @Test
    void tenPercentOffAtThreshold() {
        var lines = List.of(new CreateOrderRequest.Line("A", 10, new BigDecimal("10.00")));
        assertEquals(0, new BigDecimal("90.00").compareTo(pricing.price(lines)));
    }
}
