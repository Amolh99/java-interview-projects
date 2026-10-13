package com.example.testing;

public interface OrderRepository {

    void save(String orderId, String customerId, String transactionId);
}
