# Module 06 - Multithreading & Concurrency

Stock reservation for an order service. Many checkout threads compete for limited stock, and the code must never oversell.

## Concepts
- **Lock-free CAS loop:** `AtomicInteger.compareAndSet` retries until the reservation succeeds.
- **Thread pools:** `ExecutorService` with a fixed size bounds concurrency. Always shut it down.
- **CompletableFuture:** `supplyAsync` runs work on a pool; `thenCombine` joins two independent results.
- **Race conditions:** `if (stock >= q) stock -= q;` is a check-then-act race. CAS fixes it.

## Interview questions
1. **What is a race condition?** Correctness depends on thread timing. Check-then-act on shared state is the classic case.
2. **`synchronized` vs `AtomicInteger`?** `synchronized` blocks other threads. CAS retries without blocking and suits low-contention counters.
3. **`volatile` vs `synchronized`?** `volatile` gives visibility and ordering but not atomic compound actions.
4. **Why not `Executors.newCachedThreadPool()` for all work?** Unbounded threads can exhaust memory under load.
5. **`join()` vs `get()` on CompletableFuture?** `join` throws unchecked exceptions; `get` throws checked ones.
6. **What is a deadlock?** Two threads each hold a lock the other needs. Acquire locks in a fixed order to avoid it.
7. **Virtual threads (Java 21)?** Cheap threads for blocking I/O. Use `Executors.newVirtualThreadPerTaskExecutor()` for I/O-bound work.

## Run
```bash
mvn test
```
