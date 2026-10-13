package com.example.orders.application;

import com.example.orders.api.CreateOrderRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Component;

/** 10% off the whole order when total quantity reaches 10 units. */
@Component("bulkDiscountPricing")
public class BulkDiscountPricing implements PricingStrategy {

    private static final int THRESHOLD = 10;
    private static final BigDecimal DISCOUNT = new BigDecimal("0.10");

    private final StandardPricing base;

    public BulkDiscountPricing(StandardPricing base) {
        this.base = base;
    }

    @Override
    public BigDecimal price(List<CreateOrderRequest.Line> lines) {
        BigDecimal gross = base.price(lines);
        int units = lines.stream().mapToInt(CreateOrderRequest.Line::quantity).sum();
        if (units < THRESHOLD) {
            return gross;
        }
        return gross.multiply(BigDecimal.ONE.subtract(DISCOUNT)).setScale(2, RoundingMode.HALF_UP);
    }
}
