package com.example.reactive;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

public class OrderLookupService {

    public record Quote(String sku, BigDecimal price, boolean inStock) {}

    private final CatalogClient client;

    public OrderLookupService(CatalogClient client) {
        this.client = client;
    }

    /** Fetches price and stock for each SKU concurrently. Output order may vary. */
    public Flux<Quote> quotes(List<String> skus) {
        return Flux.fromIterable(skus)
                .flatMap(sku -> client.product(sku)
                        .zipWith(client.availableStock(sku), (p, qty) -> new Quote(sku, p.price(), qty > 0))
                        .switchIfEmpty(Mono.error(new IllegalArgumentException("unknown sku: " + sku))));
    }

    /** Same as quotes but keeps the input order, using concatMap. */
    public Flux<Quote> orderedQuotes(List<String> skus) {
        return Flux.fromIterable(skus)
                .concatMap(sku -> client.product(sku)
                        .zipWith(client.availableStock(sku), (p, qty) -> new Quote(sku, p.price(), qty > 0)));
    }

    /** Returns a fallback quote when a SKU is unknown, instead of failing the whole stream. */
    public Flux<Quote> quotesWithFallback(List<String> skus) {
        return Flux.fromIterable(skus)
                .flatMap(sku -> client.product(sku)
                        .map(p -> new Quote(sku, p.price(), true))
                        .onErrorResume(e -> Mono.just(new Quote(sku, BigDecimal.ZERO, false)))
                        .switchIfEmpty(Mono.just(new Quote(sku, BigDecimal.ZERO, false))));
    }

    public Mono<BigDecimal> orderTotal(List<String> skus) {
        return quotes(skus)
                .map(Quote::price)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
