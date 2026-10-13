package com.example.orders.service;

import com.example.orders.model.Order;
import com.example.orders.model.OrderEvent;
import com.example.orders.model.OrderItem;
import com.example.orders.model.OrderStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {

    private final OrderService service = new OrderService();

    private Order newOrder(String customer, String price, int qty) {
        return service.place(customer, List.of(new OrderItem("SKU", qty, new BigDecimal(price))));
    }

    @Test
    void totalUsesBigDecimalWithoutFloatingPointErrors() {
        Order order = service.place("alice", List.of(
                new OrderItem("A", 3, new BigDecimal("0.10")),
                new OrderItem("B", 1, new BigDecimal("0.20"))));
        assertEquals(0, new BigDecimal("0.50").compareTo(order.total()));
    }

    @Test
    void validPathPlacedPaidShipped() {
        Order order = newOrder("alice", "10", 1);
        assertEquals(OrderStatus.PAID, service.pay(order.id()).status());
        assertEquals(OrderStatus.SHIPPED, service.ship(order.id()).status());
    }

    @Test
    void cannotShipUnpaidOrder() {
        Order order = newOrder("alice", "10", 1);
        assertThrows(IllegalStateException.class, () -> service.ship(order.id()));
    }

    @Test
    void shippedOrderCannotBeCancelled() {
        Order order = newOrder("alice", "10", 1);
        service.pay(order.id());
        service.ship(order.id());
        assertThrows(IllegalStateException.class, () -> service.cancel(order.id()));
    }

    @Test
    void unknownOrderThrows() {
        assertThrows(NoSuchElementException.class, () -> service.pay("missing"));
    }

    @Test
    void itemsAreImmutable() {
        Order order = newOrder("alice", "10", 1);
        assertThrows(UnsupportedOperationException.class,
                () -> order.items().add(new OrderItem("X", 1, BigDecimal.ONE)));
    }

    @Test
    void revenueExcludesCancelledOrders() {
        Order paid = newOrder("alice", "10", 2);
        service.pay(paid.id());
        Order cancelled = newOrder("alice", "100", 1);
        service.cancel(cancelled.id());

        Map<String, BigDecimal> revenue = service.revenueByCustomer();
        assertEquals(0, new BigDecimal("20").compareTo(revenue.get("alice")));
    }

    @Test
    void placedEventCreatesOrderWithItsOwnId() {
        List<OrderItem> items = List.of(new OrderItem("SKU", 2, new BigDecimal("5")));
        Order order = service.apply(new OrderEvent.Placed("evt-order-1", "bob", items));
        assertEquals("evt-order-1", order.id());
        assertEquals(OrderStatus.PLACED, order.status());
        assertTrue(service.find("evt-order-1").isPresent());
    }

    @Test
    void duplicatePlacedEventIsRejected() {
        List<OrderItem> items = List.of(new OrderItem("SKU", 1, BigDecimal.ONE));
        service.apply(new OrderEvent.Placed("dup", "bob", items));
        assertThrows(IllegalStateException.class,
                () -> service.apply(new OrderEvent.Placed("dup", "bob", items)));
    }

    @Test
    void paidAndShippedEventsDriveTransitions() {
        Order order = newOrder("bob", "5", 1);
        assertEquals(OrderStatus.PAID,
                service.apply(new OrderEvent.Paid(order.id(), new BigDecimal("5"))).status());
        assertEquals(OrderStatus.SHIPPED,
                service.apply(new OrderEvent.Shipped(order.id(), "TRK-1")).status());
    }

    @Test
    void concurrentPlacesAreAllStored() {
        IntStream.range(0, 200).parallel().forEach(i -> newOrder("c" + (i % 5), "1", 1));
        Map<String, BigDecimal> revenue = service.revenueByCustomer();
        assertEquals(5, revenue.size());
        BigDecimal sum = revenue.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        assertEquals(0, new BigDecimal("200").compareTo(sum));
    }
}
