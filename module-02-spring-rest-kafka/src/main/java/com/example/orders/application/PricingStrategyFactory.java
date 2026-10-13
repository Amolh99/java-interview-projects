package com.example.orders.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Factory pattern: resolves the configured strategy by name. Spring injects every
 * PricingStrategy bean into the map, keyed by bean name.
 */
@Component
public class PricingStrategyFactory {

    private final PricingStrategy selected;

    public PricingStrategyFactory(Map<String, PricingStrategy> strategies,
                                  @Value("${pricing.strategy:standardPricing}") String name) {
        this.selected = strategies.get(name);
        if (selected == null) {
            throw new IllegalStateException("Unknown pricing strategy: " + name + " available: " + strategies.keySet());
        }
    }

    public PricingStrategy current() {
        return selected;
    }
}
