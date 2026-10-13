package com.example.cicd;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTotalsTest {

    @Test
    void addsTaxAndRoundsHalfUp() {
        var total = OrderTotals.withTax(List.of(new BigDecimal("10.00"), new BigDecimal("5.00")),
                new BigDecimal("0.08"));
        assertEquals(0, new BigDecimal("16.20").compareTo(total));
    }

    @Test
    void negativeTaxRateIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> OrderTotals.withTax(List.of(BigDecimal.ONE), new BigDecimal("-0.1")));
    }
}
