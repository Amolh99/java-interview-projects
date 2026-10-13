package com.example.memory;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class ListenerRegistryTest {

    @Test
    void strongRegistryKeepsListenerAlive() {
        StrongListenerRegistry registry = new StrongListenerRegistry();
        AtomicInteger calls = new AtomicInteger();
        registry.register(id -> calls.incrementAndGet());
        System.gc();
        registry.notifyPlaced("o-1");
        assertEquals(1, calls.get());
        assertEquals(1, registry.size());
    }

    @Test
    void weakRegistryDeliversToLiveListener() {
        WeakListenerRegistry registry = new WeakListenerRegistry();
        OrderListener listener = id -> { };
        registry.register(listener);
        registry.notifyPlaced("o-2");
        assertEquals(1, registry.size());
        assertNotNull(listener); // keep a strong reference for the duration of the test
    }

    @Test
    void weakRegistryPrunesCollectedListeners() {
        WeakListenerRegistry registry = new WeakListenerRegistry();
        registry.register(id -> { }); // no strong reference remains

        // Best-effort: GC is not guaranteed to run, so retry a bounded number of times.
        for (int i = 0; i < 50 && registry.size() > 0; i++) {
            System.gc();
            registry.notifyPlaced("trigger-prune");
        }
        assertEquals(0, registry.size());
    }
}
