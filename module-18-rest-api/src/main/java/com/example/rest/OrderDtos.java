package com.example.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

/** Request and response DTOs. Entities are never exposed directly. */
public final class OrderDtos {

    private OrderDtos() {}

    public record CreateOrderRequest(@NotBlank String customerId,
                                     @NotEmpty List<@Valid Item> items) {}

    public record Item(@NotBlank String sku, @Positive int quantity, @Positive BigDecimal unitPrice) {}

    public record OrderResponse(String id, String customerId, BigDecimal total, String status) {}
}
