package com.example.java8;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class OrderAnalyticsTest {

    private static final List<Order> ORDERS = List.of(
            new Order("o1", "alice", new BigDecimal("120.00"), "electronics", LocalDate.of(2026, 1, 5)),
            new Order("o2", "bob", new BigDecimal("40.50"), "books", LocalDate.of(2026, 1, 9)),
            new Order("o3", "alice", new BigDecimal("80.00"), "books", LocalDate.of(2026, 2, 1)),
            new Order("o4", "carol", new BigDecimal("300.00"), "electronics", LocalDate.of(2026, 2, 3)));

    @Test
    void revenueSumsPerCustomer() {
        Map<String, BigDecimal> revenue = OrderAnalytics.revenueByCustomer(ORDERS);
        assertEquals(0, new BigDecimal("200.00").compareTo(revenue.get("alice")));
        assertEquals(0, new BigDecimal("40.50").compareTo(revenue.get("bob")));
    }

    @Test
    void topCustomersOrderedByRevenue() {
        assertEquals(List.of("carol", "alice"), OrderAnalytics.topCustomers(ORDERS, 2));
    }

    @Test
    void largestOrderIsReturnedAsOptional() {
        assertEquals("o4", OrderAnalytics.largestOrder(ORDERS).orElseThrow().id());
        assertTrue(OrderAnalytics.largestOrder(List.of()).isEmpty());
    }

    @Test
    void countsByCategory() {
        assertEquals(Map.of("books", 2L, "electronics", 2L), OrderAnalytics.countByCategory(ORDERS));
    }

    @Test
    void averageIsRoundedHalfUpToTwoDecimals() {
        // (120 + 40.50 + 80 + 300) / 4 = 135.125 -> 135.13
        assertEquals(0, new BigDecimal("135.13").compareTo(OrderAnalytics.averageOrderValue(ORDERS)));
        assertEquals(0, BigDecimal.ZERO.compareTo(OrderAnalytics.averageOrderValue(List.of())));
    }

    @Test
    void toMapWithMergeFunctionHandlesDuplicateCustomers() {
        Map<String, BigDecimal> last = OrderAnalytics.lastOrderTotalByCustomer(ORDERS);
        assertEquals(0, new BigDecimal("80.00").compareTo(last.get("alice")));
    }

    @Test
    void plainToMapThrowsOnDuplicateKeys() {
        assertThrows(IllegalStateException.class, () ->
                ORDERS.stream().collect(java.util.stream.Collectors.toMap(Order::customer, Order::total)));
    }

    @Test
    void optionalEmailRejectsInvalidAddress() {
        Map<String, String> emails = Map.of("alice", "alice@example.com", "bob", "not-an-email");
        assertEquals(Optional.of("alice@example.com"), OrderAnalytics.customerEmail(emails, "alice"));
        assertTrue(OrderAnalytics.customerEmail(emails, "bob").isEmpty());
        assertTrue(OrderAnalytics.customerEmail(emails, "nobody").isEmpty());
    }

    @Test
    void flatMapProducesDistinctSortedSkus() {
        List<List<String>> perOrder = List.of(List.of("B", "A"), List.of("A", "C"));
        assertEquals(List.of("A", "B", "C"), OrderAnalytics.allSkus(perOrder));
    }

    @Test
    void indexByIdBuildsLookupMap() {
        assertEquals("bob", OrderAnalytics.indexById(ORDERS).get("o2").customer());
    }

    @Test
    void defaultMethodAppliesDiscount() {
        Order o = ORDERS.get(0);
        assertEquals(0, new BigDecimal("96.00").compareTo(o.discounted(new BigDecimal("0.20"))));
    }

    @Test
    void intermediateStreamOperationsAreLazy() {
        AtomicInteger peeks = new AtomicInteger();
        Stream<Order> pipeline = ORDERS.stream().peek(o -> peeks.incrementAndGet());
        assertEquals(0, peeks.get());   // nothing has run yet
        pipeline.count();               // terminal operation triggers the pipeline
        assertEquals(ORDERS.size(), peeks.get());
    }
}
