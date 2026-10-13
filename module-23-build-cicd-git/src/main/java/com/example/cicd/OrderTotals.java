package com.example.cicd;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public final class OrderTotals {

    private OrderTotals() {}

    public static BigDecimal withTax(List<BigDecimal> lines, BigDecimal taxRate) {
        if (taxRate.signum() < 0) {
            throw new IllegalArgumentException("taxRate must be >= 0");
        }
        BigDecimal subtotal = lines.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        return subtotal.add(subtotal.multiply(taxRate)).setScale(2, RoundingMode.HALF_UP);
    }
}
