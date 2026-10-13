package com.example.exceptions;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionHandlingTest {

    private static final BigDecimal TEN = new BigDecimal("10.00");

    @Test
    void retryableFailuresAreRetriedUntilSuccess() throws PaymentGatewayException {
        int[] calls = {0};
        PaymentGateway flaky = (orderId, amount) -> {
            if (++calls[0] < 3) {
                throw new PaymentGatewayException("timeout", true);
            }
            return "txn-1";
        };
        assertEquals("txn-1", new PaymentService(flaky, 3).pay("o-1", TEN));
        assertEquals(3, calls[0]);
    }

    @Test
    void nonRetryableFailureStopsImmediatelyWithChainedCause() {
        int[] calls = {0};
        PaymentGateway declined = (orderId, amount) -> {
            calls[0]++;
            throw new PaymentGatewayException("card declined", false);
        };
        PaymentFailedException ex = assertThrows(PaymentFailedException.class,
                () -> new PaymentService(declined, 5).pay("o-2", TEN));
        assertEquals(1, calls[0]);
        assertInstanceOf(PaymentGatewayException.class, ex.getCause());
    }

    @Test
    void exhaustedRetriesReportAttemptCountAndSuppressPriorFailures() {
        PaymentGateway down = (orderId, amount) -> {
            throw new PaymentGatewayException("503", true);
        };
        PaymentFailedException ex = assertThrows(PaymentFailedException.class,
                () -> new PaymentService(down, 3).pay("o-3", TEN));
        assertTrue(ex.getMessage().contains("after 3 attempt(s)"));
        assertEquals("503", ex.getCause().getMessage());
        assertEquals(2, ex.getCause().getSuppressed().length);
    }

    @Test
    void invalidAmountIsRejectedBeforeCallingGateway() {
        PaymentGateway never = (orderId, amount) -> {
            fail("gateway must not be called");
            return null;
        };
        assertThrows(InvalidOrderException.class,
                () -> new PaymentService(never, 1).pay("o-4", BigDecimal.ZERO));
    }

    @Test
    void closeFailureIsSuppressedWhenBodyAlsoFails() {
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            try (AuditTrail trail = new AuditTrail(true)) {
                trail.record("started");
                throw new RuntimeException("body failed");
            }
        });
        assertEquals("body failed", thrown.getMessage());
        assertEquals(1, thrown.getSuppressed().length);
        assertEquals("flush failed", thrown.getSuppressed()[0].getMessage());
    }

    @Test
    void closeFailureIsThrownWhenBodySucceeds() {
        IOException ex = assertThrows(IOException.class, () -> {
            try (AuditTrail trail = new AuditTrail(true)) {
                trail.record("ok");
            }
        });
        assertEquals("flush failed", ex.getMessage());
    }
}
