# Module 07 - Exception Handling

Payment handling for an order service. It covers checked vs unchecked exceptions, retries that only apply to transient failures, exception chaining, and resource cleanup with try-with-resources.

## Concepts
- **Checked exceptions:** `PaymentGatewayException` must be declared or handled. It carries a `retryable` flag.
- **Unchecked exceptions:** `InvalidOrderException` and `PaymentFailedException` signal programmer or business errors.
- **Chaining:** `PaymentFailedException` keeps the gateway failure as its cause.
- **Suppressed exceptions:** If the body and `close()` both fail, `close()` failures are attached via `getSuppressed()`.

## Interview questions
1. **Checked vs unchecked?** Checked must be handled or declared. Unchecked (RuntimeException and subclasses) need not be.
2. **Why not catch `Exception` everywhere?** It hides bugs and can swallow `InterruptedException`.
3. **What does try-with-resources guarantee?** `close()` runs in reverse order, even on exceptions.
4. **Where does a `close()` exception go if the body also threw?** It is added as suppressed to the primary exception.
5. **`finally` returns a value. What happens?** It overrides the try's return or exception. Avoid it.
6. **What is exception chaining?** Wrapping a lower-level exception as the cause to keep the original stack trace.
7. **When is retry safe?** Only for idempotent operations and transient errors. Retrying a non-idempotent charge can double-bill.

## Run
```bash
mvn test
```
