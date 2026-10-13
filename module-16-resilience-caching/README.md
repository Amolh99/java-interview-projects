# Module 16 - Resilience & Caching

Product lookups for an order service. A cache-aside layer reduces calls to a slow catalog. A retry policy handles transient errors. A circuit breaker stops calling a catalog that keeps failing.

## Concepts
- **Cache-aside:** read the cache first. On a miss, load from the source and store the result.
- **Retry with backoff:** retry only transient failures. Wait longer between attempts.
- **Circuit breaker:** CLOSED (calls pass), OPEN (calls fail fast), HALF_OPEN (one trial call).
- **Timeouts and bulkheads:** limit how long and how many calls can wait.

## Interview questions
1. **Cache-aside vs write-through?** Cache-aside loads on read. Write-through updates the cache on each write.
2. **How do you prevent a cache stampede?** Use single-flight loading (`computeIfAbsent`) so only one thread loads a key.
3. **Why add jitter to backoff?** Without it, many clients retry at the same moment and overload the service again.
4. **What does a circuit breaker protect against?** Cascading failures and wasted threads on a dependency that is down.
5. **Retry non-idempotent calls?** Not safely, unless the operation has an idempotency key.

## Run
```bash
mvn test
```
