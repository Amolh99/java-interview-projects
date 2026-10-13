package com.example.exceptions;

/** Checked exception from the external gateway. The flag tells the caller whether a retry can help. */
public class PaymentGatewayException extends Exception {

    private final boolean retryable;

    public PaymentGatewayException(String message, boolean retryable) {
        super(message);
        this.retryable = retryable;
    }

    public boolean isRetryable() {
        return retryable;
    }
}
