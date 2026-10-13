# Module 02 - Spring Boot REST + Kafka + Design Patterns

Order service built with **Java 21** and **Spring Boot 3.3.4**. It exposes a REST API, publishes `OrderPlacedEvent` to Kafka, and has a consumer that reserves stock idempotently.

## Design patterns used
| Pattern | Where |
|---|---|
| Strategy | `PricingStrategy`, `StandardPricing`, `BulkDiscountPricing` |
| Factory | `PricingStrategyFactory` selects a strategy from config |
| Ports & Adapters | `OrderEventPublisher` (port) and `KafkaOrderEventPublisher` (adapter) |
| Observer (via Kafka) | Consumers subscribe to `orders.placed` without the producer knowing them |

## Run
```bash
docker compose up -d            # local Kafka on localhost:9092
mvn test
mvn spring-boot:run
```

## Try it
```bash
curl -X POST localhost:8080/api/orders -H 'Content-Type: application/json' \
  -d '{"customerId":"alice","items":[{"sku":"BOOK-1","quantity":10,"unitPrice":12.50}]}'
```
Set `pricing.strategy=bulkDiscountPricing` to apply the 10% bulk discount.

See [INTERVIEW_NOTES.md](INTERVIEW_NOTES.md).
