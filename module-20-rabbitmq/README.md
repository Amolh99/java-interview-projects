# Module 20 - RabbitMQ

Order events are published to a topic exchange. A consumer handles each event idempotently and sends repeated or failed messages to a dead-letter queue.

## Topology
- Exchange `orders.exchange` (topic)
- Queue `inventory.orders` bound to routing key `order.placed`
- Dead-letter exchange `orders.dlx` with queue `orders.dlq`

## Concepts
- **Exchanges:** direct, topic, fanout, headers. Producers publish to exchanges, not queues.
- **Acknowledgements:** a message is removed only after ack. Unacked messages return on failure.
- **Dead-letter queues:** hold rejected messages for inspection.
- **At-least-once delivery:** consumers must be idempotent.

## Interview questions
1. **RabbitMQ vs Kafka?** RabbitMQ routes and removes messages after ack. Kafka keeps an ordered log that consumers replay.
2. **What does `requeue=false` do on reject?** The message goes to the dead-letter exchange if configured.
3. **How do you guarantee publish?** Publisher confirms, plus persistent messages on durable queues.
4. **Why are consumers idempotent?** Redelivery happens after a crash before ack.

## Run
```bash
docker compose up -d
mvn test
mvn spring-boot:run
```
