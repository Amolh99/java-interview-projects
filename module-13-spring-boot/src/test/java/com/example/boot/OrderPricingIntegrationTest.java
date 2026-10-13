package com.example.boot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/** Full context test: verifies wiring, profile selection, and typed configuration. */
@SpringBootTest
@ActiveProfiles("test")
class OrderPricingIntegrationTest {

    @Autowired OrderPricingService pricing;
    @Autowired PricingProperties properties;
    @Autowired TaxCalculator tax;

    @Test
    void contextLoadsWithTypedConfiguration() {
        assertEquals(0, new BigDecimal("0.08").compareTo(properties.taxRate()));
    }

    @Test
    void nonProdProfileUsesFlatTax() {
        assertInstanceOf(FlatTaxCalculator.class, tax);
    }

    @Test
    void totalIncludesTaxAndShipping() {
        // 100 + 8.00 tax + 4.99 shipping
        assertEquals(0, new BigDecimal("112.99").compareTo(pricing.total(new BigDecimal("100"))));
    }
}
