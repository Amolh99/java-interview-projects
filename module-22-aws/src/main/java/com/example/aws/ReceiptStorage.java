package com.example.aws;

import java.time.Duration;
import java.util.Optional;

/** Port: the application depends on this, not on the S3 SDK. */
public interface ReceiptStorage {

    void store(String orderId, byte[] pdf);

    Optional<byte[]> load(String orderId);

    /** Time-limited download link. In the S3 adapter this is an S3 presigned URL. */
    String downloadLink(String orderId, Duration validFor);
}
