package com.example.exceptions;

import java.math.BigDecimal;

public class PaymentService {

    private final PaymentGateway gateway;
    private final int maxAttempts;

    public PaymentService(PaymentGateway gateway, int maxAttempts) {
        if (maxAttempts < 1) {
            throw new IllegalArgumentException("maxAttempts must be >= 1");
        }
        this.gateway = gateway;
        this.maxAttempts = maxAttempts;
    }

    /** Retries only retryable gateway failures. Non-retryable failures fail immediately. */
    public String pay(String orderId, BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new InvalidOrderException("amount must be positive");
        }
        PaymentGatewayException last = null;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return gateway.charge(orderId, amount);
            } catch (PaymentGatewayException e) {
                if (!e.isRetryable()) {
                    throw new PaymentFailedException(orderId, attempt, e);
                }
                if (last != null && last != e) {
                    e.addSuppressed(last);
                }
                last = e;
            }
        }
        throw new PaymentFailedException(orderId, maxAttempts, last);
    }
}
