package com.example.concurrency;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/** Processes checkout requests on a bounded pool. Close it (try-with-resources) to stop the threads. */
public class OrderProcessor implements AutoCloseable {

    public record Request(String orderId, int quantity) {}

    private final ExecutorService pool;
    private final InventoryStock stock;

    public OrderProcessor(InventoryStock stock, int threads) {
        this.stock = stock;
        this.pool = Executors.newFixedThreadPool(threads);
    }

    /** Reserves stock for every request in parallel and returns the IDs that were accepted. */
    public List<String> processAll(List<Request> requests) {
        List<CompletableFuture<String>> futures = requests.stream()
                .map(r -> CompletableFuture.supplyAsync(
                        () -> stock.tryReserve(r.quantity()) ? r.orderId() : null, pool))
                .toList();
        return futures.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .toList();
    }

    /** Fetches subtotal and tax concurrently, then combines them. */
    public CompletableFuture<BigDecimal> quoteAsync(Supplier<BigDecimal> subtotal, Supplier<BigDecimal> tax) {
        CompletableFuture<BigDecimal> s = CompletableFuture.supplyAsync(subtotal, pool);
        CompletableFuture<BigDecimal> t = CompletableFuture.supplyAsync(tax, pool);
        return s.thenCombine(t, BigDecimal::add);
    }

    @Override
    public void close() {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
