package com.example.orders;

import com.example.orders.model.Order;
import com.example.orders.model.OrderItem;
import com.example.orders.service.OrderService;

import java.math.BigDecimal;
import java.util.List;

public class OrderApp {

    public static void main(String[] args) {
        OrderService service = new OrderService();

        Order o1 = service.place("alice", List.of(
                new OrderItem("BOOK-1", 2, new BigDecimal("12.50")),
                new OrderItem("PEN-7", 5, new BigDecimal("1.20"))));
        Order o2 = service.place("bob", List.of(
                new OrderItem("LAMP-3", 1, new BigDecimal("45.00"))));

        service.pay(o1.id());
        service.ship(o1.id());
        service.cancel(o2.id());

        System.out.println("Revenue by customer: " + service.revenueByCustomer());
    }
}
