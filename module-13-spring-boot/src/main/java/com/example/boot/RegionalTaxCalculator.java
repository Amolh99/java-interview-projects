package com.example.boot;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Production variant. Swap in a real tax service here; the profile keeps the choice out of the code. */
@Component
@Profile("prod")
public class RegionalTaxCalculator implements TaxCalculator {

    private final PricingProperties properties;

    public RegionalTaxCalculator(PricingProperties properties) {
        this.properties = properties;
    }

    @Override
    public BigDecimal taxFor(BigDecimal subtotal) {
        return subtotal.multiply(properties.taxRate().add(new BigDecimal("0.01")))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
