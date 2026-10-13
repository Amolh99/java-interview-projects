# Module 02 - Spring Boot, REST, Kafka, Design Patterns

## Concepts to explain
- **Dependency injection:** constructor injection makes dependencies explicit and testable without Spring.
- **Strategy + Factory:** `PricingStrategyFactory` fails fast on unknown names at startup.
- **Ports & Adapters:** the service depends on `OrderEventPublisher`; Kafka is an adapter that can be swapped.
- **Kafka keys and ordering:** ordering is guaranteed only within a partition. Keying by `orderId` keeps one order's events in order.
- **At-least-once delivery:** consumers must be idempotent (here, a processed-id set).
- **Idempotent producer:** `enable.idempotence=true` and `acks=all` prevent duplicate writes on producer retries.
- **Transactional outbox:** saving the order and publishing the event must not diverge. The outbox table fixes the dual-write problem. This module marks where it belongs.
- **Validation and errors:** `@Valid` triggers bean validation; `ProblemDetail` gives RFC 7807 responses.

## Questions
1. **Why key Kafka messages by orderId?** Same key goes to the same partition, which preserves per-order ordering.
2. **What happens if the consumer crashes after processing but before committing offsets?** The message is redelivered. Idempotency makes that safe.
3. **Why is `@Value` on the constructor parameter needed?** It binds the property into the bean. Spring Boot can't inject a plain String without it.
4. **Why not inject `KafkaTemplate` into the service directly?** It would couple business logic to Kafka. The port keeps the service testable with a Mockito mock.
5. **Strategy vs if/else?** New pricing rules are added as new classes, without changing existing code (Open/Closed principle).
6. **Why `BigDecimal` in JSON?** It keeps precision. Jackson serializes it as a number; the consumer receives exact values.
7. **What is the dual-write problem?** Writing to a DB and to Kafka separately can leave them inconsistent if one fails. Use an outbox or a transaction.
8. **Why `auto-offset-reset: earliest`?** A new consumer group starts from the beginning instead of skipping existing events.
9. **How do you test Kafka listeners?** Unit-test the method directly (as here), or use `spring-kafka-test` with `@EmbeddedKafka` for integration.
10. **What does `@RestControllerAdvice` do?** It applies exception handling across all controllers.
