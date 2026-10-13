package com.example.collections;

import java.time.Instant;

public record Shipment(String orderId, int priority, Instant createdAt) {}
