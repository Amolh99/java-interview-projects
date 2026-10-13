package com.example.modern;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/** Virtual threads (Java 21): one cheap thread per blocking task. Good for I/O-bound batches. */
public final class VirtualThreadBatch {

    private VirtualThreadBatch() {}

    public static List<String> fetchAll(List<String> orderIds) throws Exception {
        try (ExecutorService exec = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<String>> futures = orderIds.stream()
                    .map(id -> exec.submit(() -> "status:" + id))
                    .toList();
            return futures.stream().map(f -> {
                try {
                    return f.get();
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }).toList();
        }
    }
}
