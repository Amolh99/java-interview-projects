package com.example.concurrency;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class ConcurrencyTest {

    @Test
    void reserveWithinStockSucceedsAndRejectsOversell() {
        InventoryStock stock = new InventoryStock(5);
        assertTrue(stock.tryReserve(3));
        assertFalse(stock.tryReserve(3));
        assertEquals(2, stock.available());
    }

    @Test
    void exactlyStockManyParallelReservationsSucceed() {
        InventoryStock stock = new InventoryStock(500);
        long accepted = IntStream.range(0, 1_000).parallel()
                .filter(i -> stock.tryReserve(1))
                .count();
        assertEquals(500, accepted);
        assertEquals(0, stock.available());
    }

    @Test
    void processorAcceptsOnlyWhatStockAllows() {
        InventoryStock stock = new InventoryStock(3);
        try (OrderProcessor processor = new OrderProcessor(stock, 4)) {
            List<OrderProcessor.Request> requests = IntStream.range(0, 5)
                    .mapToObj(i -> new OrderProcessor.Request("o-" + i, 1))
                    .toList();
            List<String> accepted = processor.processAll(requests);
            assertEquals(3, accepted.size());
        }
        assertEquals(0, stock.available());
    }

    @Test
    void quoteCombinesSubtotalAndTaxAsynchronously() throws ExecutionException, InterruptedException {
        InventoryStock stock = new InventoryStock(0);
        try (OrderProcessor processor = new OrderProcessor(stock, 2)) {
            BigDecimal total = processor.quoteAsync(
                    () -> new BigDecimal("100.00"),
                    () -> new BigDecimal("8.25")).get();
            assertEquals(0, new BigDecimal("108.25").compareTo(total));
        }
    }

    @Test
    void zeroOrNegativeQuantityIsRejected() {
        InventoryStock stock = new InventoryStock(10);
        assertThrows(IllegalArgumentException.class, () -> stock.tryReserve(0));
    }
}
