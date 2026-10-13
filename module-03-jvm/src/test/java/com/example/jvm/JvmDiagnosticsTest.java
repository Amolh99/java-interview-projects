package com.example.jvm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JvmDiagnosticsTest {

    private final JvmDiagnostics diagnostics = new JvmDiagnostics();

    @Test
    void heapUsageIsPositive() {
        assertTrue(diagnostics.heap().usedBytes() > 0);
    }

    @Test
    void atLeastOneGarbageCollectorIsReported() {
        assertFalse(diagnostics.garbageCollectorNames().isEmpty());
    }

    @Test
    void runtimeVersionIsJava21OrNewer() {
        assertTrue(Runtime.version().feature() >= 21);
    }

    @Test
    void stringIsLoadedByBootstrapLoader() {
        assertTrue(ClassLoaderDemo.isBootstrapLoaded(String.class));
    }

    @Test
    void applicationClassIsNotBootstrapLoaded() {
        assertFalse(ClassLoaderDemo.isBootstrapLoaded(JvmDiagnostics.class));
        assertTrue(ClassLoaderDemo.describe(JvmDiagnostics.class).contains("JvmDiagnostics loaded by"));
    }
}
