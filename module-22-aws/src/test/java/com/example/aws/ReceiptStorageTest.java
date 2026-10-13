package com.example.aws;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptStorageTest {

    private final ReceiptStorage storage = new InMemoryReceiptStorage("orders-receipts");

    @Test
    void storedReceiptCanBeLoaded() {
        storage.store("o-1", new byte[] {1, 2, 3});
        assertArrayEquals(new byte[] {1, 2, 3}, storage.load("o-1").orElseThrow());
    }

    @Test
    void storedBytesAreDefensivelyCopied() {
        byte[] original = {9};
        storage.store("o-2", original);
        original[0] = 0;
        assertEquals(9, storage.load("o-2").orElseThrow()[0]);
    }

    @Test
    void missingReceiptIsEmpty() {
        assertEquals(Optional.empty(), storage.load("nope"));
    }

    @Test
    void downloadLinkContainsBucketAndExpiry() {
        storage.store("o-3", new byte[] {1});
        String link = storage.downloadLink("o-3", Duration.ofMinutes(5));
        assertTrue(link.startsWith("https://orders-receipts.s3.example.com/receipts/o-3.pdf"));
        assertTrue(link.contains("expires="));
    }

    @Test
    void downloadLinkForMissingReceiptFails() {
        assertThrows(IllegalArgumentException.class,
                () -> storage.downloadLink("missing", Duration.ofMinutes(5)));
    }
}
