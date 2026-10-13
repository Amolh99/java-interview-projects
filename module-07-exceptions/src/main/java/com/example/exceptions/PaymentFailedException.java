package com.example.exceptions;

/** Business-level failure. The gateway error stays attached as the cause. */
public class PaymentFailedException extends RuntimeException {

    public PaymentFailedException(String orderId, int attempts, Throwable cause) {
        super("payment failed for order " + orderId + " after " + attempts + " attempt(s)", cause);
    }
}
