package com.example.memory;

@FunctionalInterface
public interface OrderListener {
    void onOrderPlaced(String orderId);
}
