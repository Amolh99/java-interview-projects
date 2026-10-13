package com.example.jpa;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orders;

    public OrderService(OrderRepository orders) {
        this.orders = orders;
    }

    @Transactional
    public Long placeOrder(String customerId, String... skus) {
        if (skus.length == 0) {
            throw new IllegalArgumentException("order must have at least one line");
        }
        Order order = new Order(customerId);
        for (String sku : skus) {
            order.addLine(sku, 1);
        }
        return orders.save(order).getId();
    }

    @Transactional
    public void removeAllLines(Long orderId) {
        Order order = orders.findWithLinesById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("no order " + orderId));
        order.getLines().clear(); // orphanRemoval deletes the rows
    }
}
