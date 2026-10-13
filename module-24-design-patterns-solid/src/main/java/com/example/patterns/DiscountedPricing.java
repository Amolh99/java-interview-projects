package com.example.patterns;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Decorator: wraps any Pricing and applies a percentage discount. Decorators stack. */
public class DiscountedPricing implements Pricing {

    private final Pricing inner;
    private final BigDecimal percent;

    public DiscountedPricing(Pricing inner, BigDecimal percent) {
        this.inner = inner;
        this.percent = percent;
    }

    @Override
    public BigDecimal total(Order order) {
        BigDecimal base = inner.total(order);
        return base.multiply(BigDecimal.ONE.subtract(percent.movePointLeft(2)))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
