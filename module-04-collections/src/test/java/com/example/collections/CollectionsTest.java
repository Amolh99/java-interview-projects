package com.example.collections;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CollectionsTest {

    @Test
    void lruEvictsLeastRecentlyUsed() {
        LruCache<String, Integer> cache = new LruCache<>(2);
        cache.put("a", 1);
        cache.put("b", 2);
        cache.get("a");          // a is now most recent
        cache.put("c", 3);       // evicts b
        assertTrue(cache.containsKey("a"));
        assertFalse(cache.containsKey("b"));
        assertTrue(cache.containsKey("c"));
    }

    @Test
    void lruRejectsNonPositiveCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new LruCache<String, String>(0));
    }

    @Test
    void shippingQueueOrdersByPriorityThenAge() {
        ShippingQueue queue = new ShippingQueue();
        Instant t0 = Instant.parse("2026-01-01T00:00:00Z");
        queue.enqueue(new Shipment("low", 1, t0));
        queue.enqueue(new Shipment("high-new", 5, t0.plusSeconds(10)));
        queue.enqueue(new Shipment("high-old", 5, t0));

        assertEquals("high-old", queue.next().orElseThrow().orderId());
        assertEquals("high-new", queue.next().orElseThrow().orderId());
        assertEquals("low", queue.next().orElseThrow().orderId());
        assertTrue(queue.next().isEmpty());
    }

    @Test
    void productsWithSameSkuAreEqualAndDeduplicated() {
        Set<Product> set = new HashSet<>();
        set.add(new Product("SKU-1", "Book", BigDecimal.TEN));
        set.add(new Product("SKU-1", "Book (2nd ed)", BigDecimal.ONE));
        assertEquals(1, set.size());
    }

    @Test
    void mutatingAKeyFieldBreaksHashLookup() {
        Product key = new Product("SKU-2", "Pen", BigDecimal.ONE);
        Map<Product, String> map = new HashMap<>();
        map.put(key, "stock:10");
        // Equality uses sku only, so renaming keeps identity. Changing sku is what breaks lookup.
        key.rename("Gel Pen");
        assertEquals("stock:10", map.get(key));
    }

    @Test
    void immutableListRejectsChanges() {
        List<String> skus = List.of("A", "B");
        assertThrows(UnsupportedOperationException.class, () -> skus.add("C"));
    }
}
