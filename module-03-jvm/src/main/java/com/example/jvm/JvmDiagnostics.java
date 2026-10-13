package com.example.jvm;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.List;

/** Reads runtime JVM metrics through the platform management beans. */
public final class JvmDiagnostics {

    public record HeapSnapshot(long usedBytes, long maxBytes) {}

    public HeapSnapshot heap() {
        MemoryUsage usage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        return new HeapSnapshot(usage.getUsed(), usage.getMax());
    }

    public List<String> garbageCollectorNames() {
        return ManagementFactory.getGarbageCollectorMXBeans().stream()
                .map(GarbageCollectorMXBean::getName)
                .toList();
    }

    public String runtimeVersion() {
        return Runtime.version().toString();
    }
}
