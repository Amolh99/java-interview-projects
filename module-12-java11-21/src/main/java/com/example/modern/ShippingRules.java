package com.example.modern;

import java.time.DayOfWeek;

/** Switch expressions (Java 14+) with arrow labels and yield. */
public final class ShippingRules {

    private ShippingRules() {}

    public static int deliveryDays(String region, DayOfWeek orderedOn) {
        int base = switch (region) {
            case "LOCAL" -> 1;
            case "NATIONAL" -> 3;
            case "INTERNATIONAL" -> {
                int days = 7;
                yield orderedOn == DayOfWeek.FRIDAY ? days + 2 : days;
            }
            default -> throw new IllegalArgumentException("unknown region: " + region);
        };
        return orderedOn == DayOfWeek.SUNDAY ? base + 1 : base;
    }
}
