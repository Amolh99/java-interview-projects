package com.example.orders.model;

public enum OrderStatus {
    PLACED,
    PAID,
    SHIPPED,
    CANCELLED;

    public boolean isTerminal() {
        return this == SHIPPED || this == CANCELLED;
    }
}
