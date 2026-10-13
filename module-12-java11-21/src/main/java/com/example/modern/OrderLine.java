package com.example.modern;

import java.math.BigDecimal;

/** Record: immutable value with validation in the compact constructor. */
public record OrderLine(String sku, int quantity, BigDecimal unitPrice) {

    public OrderLine {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be > 0");
        }
        if (unitPrice == null || unitPrice.signum() < 0) {
            throw new IllegalArgumentException("unitPrice must be >= 0");
        }
    }

    public BigDecimal lineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
