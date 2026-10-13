package com.example.concurrency;

import java.util.concurrent.atomic.AtomicInteger;

/** Lock-free stock counter. The CAS loop makes check-and-decrement atomic, so stock never goes negative. */
public class InventoryStock {

    private final AtomicInteger available;

    public InventoryStock(int initial) {
        if (initial < 0) {
            throw new IllegalArgumentException("initial stock must be >= 0");
        }
        this.available = new AtomicInteger(initial);
    }

    public boolean tryReserve(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be > 0");
        }
        while (true) {
            int current = available.get();
            if (current < quantity) {
                return false;
            }
            if (available.compareAndSet(current, current - quantity)) {
                return true;
            }
            // Another thread changed the value between get and CAS: retry.
        }
    }

    public int available() {
        return available.get();
    }
}
