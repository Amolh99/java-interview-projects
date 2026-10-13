package com.example.testing;

import java.math.BigDecimal;

public interface PaymentGateway {

    /** Returns a transaction id, or throws if the charge is declined. */
    String charge(String customerId, BigDecimal amount);
}
