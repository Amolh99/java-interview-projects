package com.example.orders.application;

import com.example.orders.api.CreateOrderRequest;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

@Component("standardPricing")
public class StandardPricing implements PricingStrategy {

    @Override
    public BigDecimal price(List<CreateOrderRequest.Line> lines) {
        return lines.stream()
                .map(l -> l.unitPrice().multiply(BigDecimal.valueOf(l.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
