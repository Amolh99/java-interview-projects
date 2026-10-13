package com.example.resilience;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class ResilienceCachingTest {

    @Test
    void cacheLoadsOnceThenServesFromMemory() {
        AtomicInteger loads = new AtomicInteger();
        CacheAside<String, String> cache = new CacheAside<>(sku -> {
            loads.incrementAndGet();
            return "product-" + sku;
        });
        assertEquals("product-BOOK", cache.get("BOOK"));
        assertEquals("product-BOOK", cache.get("BOOK"));
        assertEquals(1, loads.get());
    }

    @Test
    void evictForcesReload() {
        AtomicInteger loads = new AtomicInteger();
        CacheAside<String, String> cache = new CacheAside<>(sku -> {
            loads.incrementAndGet();
            return sku;
        });
        cache.get("A");
        cache.evict("A");
        cache.get("A");
        assertEquals(2, loads.get());
    }

    @Test
    void retryEventuallySucceedsOnTransientFailure() throws Exception {
        AtomicInteger calls = new AtomicInteger();
        RetryPolicy policy = new RetryPolicy(3, 0);
        String result = policy.execute(() -> {
            if (calls.incrementAndGet() < 3) {
                throw new IllegalStateException("timeout");
            }
            return "ok";
        });
        assertEquals("ok", result);
        assertEquals(3, calls.get());
    }

    @Test
    void retryGivesUpAfterMaxAttempts() {
        AtomicInteger calls = new AtomicInteger();
        RetryPolicy policy = new RetryPolicy(2, 0);
        assertThrows(IllegalStateException.class, () -> policy.execute(() -> {
            calls.incrementAndGet();
            throw new IllegalStateException("down");
        }));
        assertEquals(2, calls.get());
    }

    @Test
    void circuitOpensAfterThresholdAndFailsFast() {
        Clock clock = Clock.fixed(Instant.parse("2026-01-01T00:00:00Z"), ZoneOffset.UTC);
        CircuitBreaker breaker = new CircuitBreaker(2, Duration.ofSeconds(30), clock);
        AtomicInteger downstreamCalls = new AtomicInteger();

        for (int i = 0; i < 2; i++) {
            assertThrows(IllegalStateException.class, () -> breaker.call(() -> {
                downstreamCalls.incrementAndGet();
                throw new IllegalStateException("down");
            }));
        }
        assertEquals(CircuitBreaker.State.OPEN, breaker.state());
        assertThrows(IllegalStateException.class, () -> breaker.call(() -> {
            downstreamCalls.incrementAndGet();
            return "never";
        }));
        assertEquals(2, downstreamCalls.get()); // open circuit did not call downstream
    }

    @Test
    void circuitHalfOpensAfterCoolDown() {
        Instant start = Instant.parse("2026-01-01T00:00:00Z");
        Instant[] now = {start};
        Clock moving = new Clock() {
            @Override public ZoneId getZone() { return ZoneOffset.UTC; }
            @Override public Clock withZone(ZoneId zone) { return this; }
            @Override public Instant instant() { return now[0]; }
        };
        CircuitBreaker breaker = new CircuitBreaker(1, Duration.ofSeconds(30), moving);
        assertThrows(IllegalStateException.class, () -> breaker.call(() -> {
            throw new IllegalStateException("down");
        }));
        assertEquals(CircuitBreaker.State.OPEN, breaker.state());

        now[0] = start.plusSeconds(31);
        assertEquals(CircuitBreaker.State.HALF_OPEN, breaker.state());
    }
}
