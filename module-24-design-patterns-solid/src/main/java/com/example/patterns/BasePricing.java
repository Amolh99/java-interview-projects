package com.example.patterns;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Base pricing: sum of line prices. Concrete component for the Decorator pattern. */
public class BasePricing implements Pricing {

    @Override
    public BigDecimal total(Order order) {
        return order.lines().stream().reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
