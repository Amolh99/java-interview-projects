package com.example.reactive;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Simulates a non-blocking catalog and inventory API. In a real service these would be WebClient calls.
 */
public class CatalogClient {

    private final Map<String, Product> catalog;
    private final Map<String, Integer> stock;

    public CatalogClient(Map<String, Product> catalog, Map<String, Integer> stock) {
        this.catalog = catalog;
        this.stock = stock;
    }

    public Mono<Product> product(String sku) {
        return Mono.justOrEmpty(catalog.get(sku));
    }

    public Mono<Integer> availableStock(String sku) {
        return Mono.fromSupplier(() -> stock.getOrDefault(sku, 0));
    }

    public Flux<Product> products(Iterable<String> skus) {
        return Flux.fromIterable(skus).flatMap(this::product);
    }
}
