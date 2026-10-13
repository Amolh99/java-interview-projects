package com.example.memory;

import java.util.ArrayList;
import java.util.List;

/** Leaks: every registered listener stays reachable until someone calls unregister. */
public class StrongListenerRegistry {

    private final List<OrderListener> listeners = new ArrayList<>();

    public void register(OrderListener listener) {
        listeners.add(listener);
    }

    public void notifyPlaced(String orderId) {
        listeners.forEach(l -> l.onOrderPlaced(orderId));
    }

    public int size() {
        return listeners.size();
    }
}
