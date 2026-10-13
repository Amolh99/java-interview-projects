package com.example.patterns;

import java.math.BigDecimal;

/** Interface Segregation: one small interface. Decorators implement the same contract. */
public interface Pricing {

    BigDecimal total(Order order);
}
