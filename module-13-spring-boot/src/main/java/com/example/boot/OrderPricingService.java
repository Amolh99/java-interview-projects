package com.example.boot;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class OrderPricingService {

    private final TaxCalculator tax;
    private final PricingProperties properties;

    public OrderPricingService(TaxCalculator tax, PricingProperties properties) {
        this.tax = tax;
        this.properties = properties;
    }

    public BigDecimal total(BigDecimal subtotal) {
        BigDecimal taxAmount = tax.taxFor(subtotal);
        return subtotal.add(taxAmount).add(properties.shippingFee()).setScale(2, RoundingMode.HALF_UP);
    }
}
