package com.example.functional;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class FunctionalToolsTest {

    @Test
    void pipelineAppliesRulesInOrder() {
        // 100 -> 10% off = 90.00 -> +5 fee = 95.00
        var pricing = FunctionalTools.pipeline(List.of(
                FunctionalTools.percentOff(new BigDecimal("10")),
                FunctionalTools.flatFee(new BigDecimal("5"))));
        assertEquals(0, new BigDecimal("95.00").compareTo(pricing.apply(new BigDecimal("100"))));
    }

    @Test
    void orderOfRulesMatters() {
        // Fee first: (100 + 5) * 0.9 = 94.50
        var pricing = FunctionalTools.pipeline(List.of(
                FunctionalTools.flatFee(new BigDecimal("5")),
                FunctionalTools.percentOff(new BigDecimal("10"))));
        assertEquals(0, new BigDecimal("94.50").compareTo(pricing.apply(new BigDecimal("100"))));
    }

    @Test
    void emptyPipelineIsIdentity() {
        assertEquals(0, BigDecimal.TEN.compareTo(FunctionalTools.pipeline(List.of()).apply(BigDecimal.TEN)));
    }

    @Test
    void allOfCombinesPredicates() {
        Predicate<Integer> valid = FunctionalTools.allOf(List.of(q -> q > 0, q -> q <= 10));
        assertTrue(valid.test(5));
        assertFalse(valid.test(0));
        assertFalse(valid.test(11));
        assertFalse(valid.negate().test(5));
    }

    @Test
    void memoizeCallsFunctionOncePerInput() {
        AtomicInteger calls = new AtomicInteger();
        var square = FunctionalTools.memoize((Integer x) -> {
            calls.incrementAndGet();
            return x * x;
        });
        assertEquals(49, square.apply(7));
        assertEquals(49, square.apply(7));
        assertEquals(1, calls.get());
    }

    @Test
    void retryReturnsFirstSuccess() {
        AtomicInteger calls = new AtomicInteger();
        String result = FunctionalTools.retry(() -> {
            if (calls.incrementAndGet() < 3) {
                throw new IllegalStateException("transient");
            }
            return "ok";
        }, 5);
        assertEquals("ok", result);
        assertEquals(3, calls.get());
    }

    @Test
    void retryRethrowsLastFailureWithSuppressed() {
        AtomicInteger calls = new AtomicInteger();
        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
                FunctionalTools.retry(() -> {
                    throw new IllegalStateException("attempt " + calls.incrementAndGet());
                }, 3));
        assertEquals(3, calls.get());
        assertEquals("attempt 3", ex.getMessage());
        assertEquals(2, ex.getSuppressed().length);
    }

    @Test
    void curriedAddPartiallyApplies() {
        var addFive = FunctionalTools.curriedAdd().apply(5);
        assertEquals(12, addFive.apply(7));
    }
}
