package com.example.resilience;

import java.util.concurrent.Callable;

/** Retries a call up to maxAttempts times. Waits baseDelayMs * attempt between tries. */
public final class RetryPolicy {

    private final int maxAttempts;
    private final long baseDelayMs;

    public RetryPolicy(int maxAttempts, long baseDelayMs) {
        if (maxAttempts < 1) {
            throw new IllegalArgumentException("maxAttempts must be >= 1");
        }
        this.maxAttempts = maxAttempts;
        this.baseDelayMs = baseDelayMs;
    }

    public <T> T execute(Callable<T> action) throws Exception {
        Exception last = null;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return action.call();
            } catch (Exception e) {
                last = e;
                if (attempt < maxAttempts && baseDelayMs > 0) {
                    Thread.sleep(baseDelayMs * attempt);
                }
            }
        }
        throw last;
    }
}
