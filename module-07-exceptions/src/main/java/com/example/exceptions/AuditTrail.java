package com.example.exceptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Resource whose close() can fail, used to demonstrate suppressed exceptions. */
public class AuditTrail implements AutoCloseable {

    private final List<String> entries = new ArrayList<>();
    private final boolean failOnClose;

    public AuditTrail(boolean failOnClose) {
        this.failOnClose = failOnClose;
    }

    public void record(String entry) {
        entries.add(entry);
    }

    public List<String> entries() {
        return List.copyOf(entries);
    }

    @Override
    public void close() throws IOException {
        if (failOnClose) {
            throw new IOException("flush failed");
        }
    }
}
