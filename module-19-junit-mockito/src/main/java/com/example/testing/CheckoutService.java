package com.example.testing;

import java.math.BigDecimal;
import java.util.UUID;

public class CheckoutService {

    private final PaymentGateway payments;
    private final OrderRepository orders;

    public CheckoutService(PaymentGateway payments, OrderRepository orders) {
        this.payments = payments;
        this.orders = orders;
    }

    /** Charges first, then saves. A declined charge is never saved as an order. */
    public String checkout(String customerId, BigDecimal total) {
        if (total == null || total.signum() <= 0) {
            throw new IllegalArgumentException("total must be positive");
        }
        String transactionId = payments.charge(customerId, total);
        String orderId = UUID.randomUUID().toString();
        orders.save(orderId, customerId, transactionId);
        return orderId;
    }
}
