package com.example.microservices;

/** Port: the order service depends on this, not on HTTP. */
public interface InventoryGateway {

    boolean isAvailable(String sku, int quantity);
}
