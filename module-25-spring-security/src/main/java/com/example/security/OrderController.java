package com.example.security;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class OrderController {

    @GetMapping("/api/public/status")
    public Map<String, String> status() {
        return Map.of("status", "UP");
    }

    /** Object-level check: a customer can read only the orders that belong to them. */
    @GetMapping("/api/customers/{customerId}/orders")
    @PreAuthorize("#customerId == authentication.name or hasRole('ADMIN')")
    public Map<String, Object> ordersFor(@PathVariable String customerId) {
        return Map.of("customerId", customerId, "orders", java.util.List.of());
    }

    @GetMapping("/api/admin/reports")
    public Map<String, String> adminReport() {
        return Map.of("report", "revenue");
    }
}
