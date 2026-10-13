package com.example.collections;

import java.util.Comparator;
import java.util.Optional;
import java.util.PriorityQueue;

/** Higher priority ships first. Ties go to the older shipment. */
public class ShippingQueue {

    private static final Comparator<Shipment> ORDER =
            Comparator.comparingInt(Shipment::priority).reversed()
                    .thenComparing(Shipment::createdAt);

    private final PriorityQueue<Shipment> queue = new PriorityQueue<>(ORDER);

    public void enqueue(Shipment shipment) {
        queue.offer(shipment);
    }

    public Optional<Shipment> next() {
        return Optional.ofNullable(queue.poll());
    }

    public int size() {
        return queue.size();
    }
}
