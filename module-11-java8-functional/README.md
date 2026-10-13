# Module 11 - Java 8 Functional Questions

Functional building blocks applied to order processing: pricing rules as composed functions, validation with predicates, memoization, retry with suppliers, and currying.

## Concepts
- **Function composition:** `andThen` and `compose` chain pricing rules.
- **Predicate combinators:** `and`, `or`, `negate` build validation rules.
- **Memoization:** `computeIfAbsent` on a `ConcurrentHashMap` caches results.
- **Higher-order functions:** `retry` takes a `Supplier` and returns its result.
- **Currying:** a function that returns a function.

## Interview questions
1. **`andThen` vs `compose`?** `f.andThen(g)` runs f first. `f.compose(g)` runs g first.
2. **Why are functional interfaces the target of lambdas?** A lambda must match exactly one abstract method.
3. **Is `Function.identity()` safe to use as a fold seed?** Yes, it is the neutral element for composition.
4. **Pitfall with memoization?** It keeps every result in memory. Bound the cache for unbounded inputs.
5. **Effectively final?** Lambdas can capture only local variables that never change. This is why tests use `int[]` or `AtomicInteger` counters.
6. **What is currying used for?** Partial application, e.g. building a `Function<BigDecimal, BigDecimal>` for a fixed discount.

## Run
```bash
mvn test
```
