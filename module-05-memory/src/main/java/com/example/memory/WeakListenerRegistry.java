package com.example.memory;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds listeners weakly. A listener that nobody else references can be garbage collected,
 * and its slot is pruned on the next notification.
 */
public class WeakListenerRegistry {

    private final List<WeakReference<OrderListener>> listeners = new ArrayList<>();

    public synchronized void register(OrderListener listener) {
        listeners.add(new WeakReference<>(listener));
    }

    public synchronized void notifyPlaced(String orderId) {
        List<OrderListener> live = new ArrayList<>();
        listeners.removeIf(ref -> {
            OrderListener l = ref.get();
            if (l == null) {
                return true; // collected: prune
            }
            live.add(l);
            return false;
        });
        live.forEach(l -> l.onOrderPlaced(orderId));
    }

    public synchronized int size() {
        return listeners.size();
    }
}
