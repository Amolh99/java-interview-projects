package com.example.exceptions;

import java.math.BigDecimal;

@FunctionalInterface
public interface PaymentGateway {

    String charge(String orderId, BigDecimal amount) throws PaymentGatewayException;
}
