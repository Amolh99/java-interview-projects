package com.example.collections;

import java.math.BigDecimal;
import java.util.Objects;

/** Identity is the SKU. Name and price can change without changing identity. */
public final class Product {

    private final String sku;
    private String name;
    private BigDecimal price;

    public Product(String sku, String name, BigDecimal price) {
        this.sku = Objects.requireNonNull(sku);
        this.name = name;
        this.price = price;
    }

    public String sku() { return sku; }
    public String name() { return name; }
    public BigDecimal price() { return price; }

    public void rename(String newName) { this.name = newName; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product other)) return false;
        return sku.equals(other.sku);
    }

    @Override
    public int hashCode() {
        return sku.hashCode();
    }
}
