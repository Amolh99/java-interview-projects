package com.example.patterns;

import java.math.BigDecimal;

/** Strategy: shipping rules are interchangeable. Factory Method picks one by region. */
public interface ShippingPolicy {

    BigDecimal fee(Order order);

    static ShippingPolicy forRegion(String region) {
        return switch (region) {
            case "LOCAL" -> order -> BigDecimal.ZERO;
            case "NATIONAL" -> order -> new BigDecimal("4.99");
            case "INTERNATIONAL" -> order -> new BigDecimal("19.99");
            default -> throw new IllegalArgumentException("unknown region: " + region);
        };
    }
}
