package com.example.microservices;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {

    private final Set<String> inStock = Set.of("BOOK");
    private final OrderService service = new OrderService((sku, qty) -> inStock.contains(sku));

    @Test
    void acceptsOrderWhenAllLinesAvailable() {
        var placed = service.place(List.of(new OrderService.Line("BOOK", 1)));
        assertNotNull(placed.orderId());
        assertEquals(1, placed.lines().size());
    }

    @Test
    void rejectsOrderWhenAnyLineIsOutOfStock() {
        var lines = List.of(new OrderService.Line("BOOK", 1), new OrderService.Line("LAMP", 1));
        assertThrows(IllegalStateException.class, () -> service.place(lines));
    }

    @Test
    void rejectsEmptyOrder() {
        assertThrows(IllegalArgumentException.class, () -> service.place(List.of()));
    }
}
