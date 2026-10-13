package com.example.java8;

import java.math.BigDecimal;
import java.math.RoundingMode;

public interface Discountable {

    BigDecimal price();

    /** Default method: implementers get this behaviour for free and can still override it. */
    default BigDecimal discounted(BigDecimal percent) {
        return price().multiply(BigDecimal.ONE.subtract(percent)).setScale(2, RoundingMode.HALF_UP);
    }
}
