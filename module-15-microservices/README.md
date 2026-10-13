# Module 15 - Microservices

Order service calls an inventory service over HTTP before accepting an order. The inventory call sits behind an interface, so the order logic is testable without a network.

## Concepts
- **Service boundaries:** each service owns its data. The order service asks inventory, never reads its database.
- **Synchronous calls:** `RestClient` (Spring 6.1+) for HTTP.
- **Ports and adapters:** `InventoryGateway` is the port. `RestInventoryGateway` is the adapter.
- **Service discovery and config:** in Docker Compose, services reach each other by service name.

## Interview questions
1. **Monolith vs microservices?** Microservices give independent deploys and scaling, at the cost of network calls, data consistency work, and operations.
2. **Why not share a database between services?** It couples schemas and deploys. Each service should own its data.
3. **What is a distributed transaction problem?** One business action spans services. Use sagas or events rather than two-phase commit.
4. **Sync vs async communication?** Sync (HTTP) is simple but couples availability. Async (Kafka, RabbitMQ) decouples it.
5. **What is an API gateway?** A single entry point for routing, auth, and rate limiting.

## Run
```bash
mvn test
docker compose up --build
```
