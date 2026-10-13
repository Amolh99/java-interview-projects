package com.example.aws;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/** Test adapter. Shaped like an S3 bucket keyed by object key. */
public class InMemoryReceiptStorage implements ReceiptStorage {

    private final String bucket;
    private final Map<String, byte[]> objects = new ConcurrentHashMap<>();

    public InMemoryReceiptStorage(String bucket) {
        this.bucket = Objects.requireNonNull(bucket);
    }

    private static String key(String orderId) {
        return "receipts/" + orderId + ".pdf";
    }

    @Override
    public void store(String orderId, byte[] pdf) {
        objects.put(key(orderId), pdf.clone());
    }

    @Override
    public Optional<byte[]> load(String orderId) {
        return Optional.ofNullable(objects.get(key(orderId))).map(byte[]::clone);
    }

    @Override
    public String downloadLink(String orderId, Duration validFor) {
        if (!objects.containsKey(key(orderId))) {
            throw new IllegalArgumentException("no receipt for " + orderId);
        }
        long expires = System.currentTimeMillis() + validFor.toMillis();
        return "https://" + bucket + ".s3.example.com/" + key(orderId) + "?expires=" + expires;
    }
}
