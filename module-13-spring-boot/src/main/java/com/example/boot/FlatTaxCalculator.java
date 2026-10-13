package com.example.boot;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@Profile("!prod")
public class FlatTaxCalculator implements TaxCalculator {

    private final PricingProperties properties;

    public FlatTaxCalculator(PricingProperties properties) {
        this.properties = properties;
    }

    @Override
    public BigDecimal taxFor(BigDecimal subtotal) {
        return subtotal.multiply(properties.taxRate()).setScale(2, RoundingMode.HALF_UP);
    }
}
