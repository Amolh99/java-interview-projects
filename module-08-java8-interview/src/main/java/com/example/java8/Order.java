package com.example.java8;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Order(String id, String customer, BigDecimal total, String category, LocalDate date)
        implements Discountable {

    @Override
    public BigDecimal price() {
        return total;
    }
}
