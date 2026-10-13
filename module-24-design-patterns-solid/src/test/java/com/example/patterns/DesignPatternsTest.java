package com.example.patterns;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DesignPatternsTest {

    private static Order twoLines() {
        return Order.builder("o-1", "alice")
                .addLine(new BigDecimal("10.00"))
                .addLine(new BigDecimal("20.00"))
                .note(null)
                .build();
    }

    @Test
    void builderRejectsEmptyOrder() {
        assertThrows(IllegalStateException.class, () -> Order.builder("o-2", "bob").build());
    }

    @Test
    void builderNormalisesNullNoteToEmpty() {
        assertEquals("", twoLines().note());
    }

    @Test
    void decoratorsStackDiscounts() {
        // 30.00 -> 10% off = 27.00 -> 50% off = 13.50
        Pricing stacked = new DiscountedPricing(new DiscountedPricing(new BasePricing(),
                new BigDecimal("10")), new BigDecimal("50"));
        assertEquals(0, new BigDecimal("13.50").compareTo(stacked.total(twoLines())));
    }

    @Test
    void observersReceivePlacedEvents() {
        OrderEvents events = new OrderEvents();
        List<String> seen = new ArrayList<>();
        events.subscribe(o -> seen.add("inventory:" + o.id()));
        events.subscribe(o -> seen.add("email:" + o.id()));
        events.placed(twoLines());
        assertEquals(List.of("inventory:o-1", "email:o-1"), seen);
    }

    @Test
    void shippingFactoryReturnsPolicyPerRegion() {
        assertEquals(0, BigDecimal.ZERO.compareTo(ShippingPolicy.forRegion("LOCAL").fee(twoLines())));
        assertEquals(0, new BigDecimal("19.99").compareTo(
                ShippingPolicy.forRegion("INTERNATIONAL").fee(twoLines())));
        assertThrows(IllegalArgumentException.class, () -> ShippingPolicy.forRegion("MARS"));
    }
}
