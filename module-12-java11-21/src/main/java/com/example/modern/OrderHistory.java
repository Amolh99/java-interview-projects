package com.example.modern;

import java.util.ArrayList;
import java.util.List;

/** Sequenced collections (Java 21): getFirst, getLast, and reversed() on a List. */
public final class OrderHistory {

    private final List<String> events = new ArrayList<>();

    public void record(String event) {
        events.add(event);
    }

    public String latest() {
        return events.getLast();
    }

    public String earliest() {
        return events.getFirst();
    }

    public List<String> newestFirst() {
        return events.reversed();
    }
}
