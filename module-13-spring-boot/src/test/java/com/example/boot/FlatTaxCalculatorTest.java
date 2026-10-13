package com.example.boot;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/** Plain unit test. Constructor injection makes this possible without starting Spring. */
class FlatTaxCalculatorTest {

    @Test
    void taxIsRoundedHalfUp() {
        var props = new PricingProperties(new BigDecimal("0.075"), BigDecimal.ZERO);
        // 10.00 * 0.075 = 0.75
        assertEquals(0, new BigDecimal("0.75").compareTo(new FlatTaxCalculator(props).taxFor(BigDecimal.TEN)));
    }
}
