package com.example.rabbit;

import java.math.BigDecimal;

public record OrderPlacedMessage(String orderId, String customerId, BigDecimal total) {}
