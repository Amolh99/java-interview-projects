package com.example.k8s;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HealthStatusTest {

    @Test
    void livenessStaysUpEvenWhenDatabaseIsDown() {
        assertEquals(HealthStatus.Status.UP, new HealthStatus(false, false).live());
    }

    @Test
    void readinessRequiresDatabaseAndWarmup() {
        assertEquals(HealthStatus.Status.UP, new HealthStatus(true, true).ready());
        assertEquals(HealthStatus.Status.DOWN, new HealthStatus(false, true).ready());
        assertEquals(HealthStatus.Status.DOWN, new HealthStatus(true, false).ready());
    }
}
