package com.example.boot;

import java.math.BigDecimal;

/** Strategy interface. Two beans implement it and a profile decides which one is active. */
public interface TaxCalculator {

    BigDecimal taxFor(BigDecimal subtotal);
}
