package com.example.microservices;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    public record Line(String sku, int quantity) {}

    public record PlacedOrder(String orderId, List<Line> lines) {}

    private final InventoryGateway inventory;

    public OrderService(InventoryGateway inventory) {
        this.inventory = inventory;
    }

    /** Rejects the order if any line lacks stock. Stock is checked per line, not reserved, in this demo. */
    public PlacedOrder place(List<Line> lines) {
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("order must have at least one line");
        }
        for (Line line : lines) {
            if (!inventory.isAvailable(line.sku(), line.quantity())) {
                throw new IllegalStateException("insufficient stock for " + line.sku());
            }
        }
        return new PlacedOrder(UUID.randomUUID().toString(), List.copyOf(lines));
    }
}
