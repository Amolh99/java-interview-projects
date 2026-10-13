# Module 14 - Spring WebFlux

Reactive order-lookup service. It uses `Mono` and `Flux`, non-blocking composition, and `StepVerifier` tests. No thread blocks while waiting for stock or pricing.

## Concepts
- **Publisher types:** `Mono` emits 0 or 1 item. `Flux` emits 0..N.
- **Lazy and cold:** nothing runs until subscribed. Each subscription re-runs the pipeline.
- **Backpressure:** the subscriber controls how much data flows.
- **Error handling:** `onErrorResume` and `onErrorReturn` recover without breaking the stream.
- **Concurrency:** `flatMap` runs inner publishers concurrently. `concatMap` keeps order.

## Interview questions
1. **Mono vs Flux?** Mono is 0 or 1 element. Flux is 0 to N.
2. **Is WebFlux faster than MVC?** Not necessarily. It helps with high concurrency and slow I/O, and it adds complexity.
3. **`flatMap` vs `concatMap`?** `flatMap` may interleave results. `concatMap` preserves order.
4. **What happens if you call `block()` inside WebFlux?** It blocks an event-loop thread, which can stall the server.
5. **Cold vs hot publisher?** Cold restarts for each subscriber. Hot shares the same stream.
6. **How do you test a reactive pipeline?** `StepVerifier` from `reactor-test`.

## Run
```bash
mvn test
```
