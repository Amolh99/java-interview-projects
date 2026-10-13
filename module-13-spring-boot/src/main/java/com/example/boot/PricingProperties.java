package com.example.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/** Typed configuration bound from pricing.* in application.yml. */
@Validated
@ConfigurationProperties(prefix = "pricing")
public record PricingProperties(
        @NotNull @DecimalMin("0") BigDecimal taxRate,
        @NotNull @DecimalMin("0") BigDecimal shippingFee) {}
