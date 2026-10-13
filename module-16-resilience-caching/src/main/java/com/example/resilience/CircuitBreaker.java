package com.example.resilience;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Callable;

/** Simple circuit breaker: opens after N consecutive failures, allows a trial call after the cool-down. */
public class CircuitBreaker {

    public enum State { CLOSED, OPEN, HALF_OPEN }

    private final int failureThreshold;
    private final Duration coolDown;
    private final Clock clock;

    private State state = State.CLOSED;
    private int consecutiveFailures = 0;
    private Instant openedAt;

    public CircuitBreaker(int failureThreshold, Duration coolDown, Clock clock) {
        this.failureThreshold = failureThreshold;
        this.coolDown = coolDown;
        this.clock = clock;
    }

    public synchronized State state() {
        if (state == State.OPEN && Instant.now(clock).isAfter(openedAt.plus(coolDown))) {
            state = State.HALF_OPEN;
        }
        return state;
    }

    public <T> T call(Callable<T> action) throws Exception {
        if (state() == State.OPEN) {
            throw new IllegalStateException("circuit open: failing fast");
        }
        try {
            T result = action.call();
            onSuccess();
            return result;
        } catch (Exception e) {
            onFailure();
            throw e;
        }
    }

    private synchronized void onSuccess() {
        consecutiveFailures = 0;
        state = State.CLOSED;
    }

    private synchronized void onFailure() {
        consecutiveFailures++;
        if (state == State.HALF_OPEN || consecutiveFailures >= failureThreshold) {
            state = State.OPEN;
            openedAt = Instant.now(clock);
        }
    }
}
