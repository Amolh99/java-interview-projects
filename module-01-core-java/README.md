# Module 01 - Core Java: E-commerce Order Service

**Topics:** records, sealed interfaces, pattern-matching `switch`, streams & collectors, `Optional`, immutability, `BigDecimal` money handling, `ConcurrentHashMap`, state machines, JUnit 5.

**Runtime:** Java 21 (`--release 21`). No frameworks, so the code runs with the JDK alone.

## Domain
An order service that places orders, takes payments, ships, and cancels them, plus revenue reporting per customer.

## Run
```bash
cd module-01-core-java
mvn test
```

## Layout
```
src/main/java/com/example/orders/
  model/     Order, OrderItem, OrderStatus, OrderEvent (sealed)
  service/   OrderService (state transitions, reporting)
  OrderApp.java   demo main
src/test/java/com/example/orders/service/OrderServiceTest.java
INTERVIEW_NOTES.md
```

See [INTERVIEW_NOTES.md](INTERVIEW_NOTES.md) for the concepts and Q&A.
