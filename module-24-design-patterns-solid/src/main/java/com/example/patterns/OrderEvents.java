package com.example.patterns;

import java.util.ArrayList;
import java.util.List;

/** Observer: subscribers react to order events without the order knowing who they are. */
public class OrderEvents {

    public interface Listener {
        void onPlaced(Order order);
    }

    private final List<Listener> listeners = new ArrayList<>();

    public void subscribe(Listener listener) {
        listeners.add(listener);
    }

    public void placed(Order order) {
        listeners.forEach(l -> l.onPlaced(order));
    }
}
