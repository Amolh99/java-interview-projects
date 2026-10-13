# Module 01 - Core Java Interview Notes

## Concepts to explain
- **Records:** immutable data carriers; compact constructors validate input; auto `equals`, `hashCode`, `toString`.
- **Sealed interfaces:** restrict subtypes via `permits`; combined with pattern-matching `switch` gives compile-time exhaustiveness.
- **BigDecimal for money:** `double` gives binary rounding errors (`0.1 + 0.2 != 0.3`). Build from `String` constructors, not `double`.
- **Immutability:** `List.copyOf` makes a defensive, unmodifiable copy; state changes return new objects.
- **ConcurrentHashMap.compute:** atomic read-check-write per key, so two threads cannot both pay the same order.
- **Streams & Collectors:** `groupingBy` with a `TreeMap` supplier and `reducing` for per-customer totals.
- **Enums with behaviour:** `OrderStatus.isTerminal()`; the `switch` in `isAllowed` is exhaustive without `default`.

## Questions
1. **Why not use `double` for prices?**  
   Binary floating point cannot represent most decimal fractions exactly. Use `BigDecimal` and compare with `compareTo`, not `equals` (scale matters in `equals`).
2. **Why is `Order` a record and not a class?**  
   It is a value snapshot. Records make immutability and value equality the default. Changing status creates a new instance.
3. **What does `List.copyOf` protect against?**  
   A caller keeping a reference to the list and mutating it after construction.
4. **`HashMap` vs `ConcurrentHashMap`?**  
   `ConcurrentHashMap` gives thread-safe operations without locking the whole map. `compute` is atomic per key. Note: `HashMap` may corrupt or lose entries under concurrent writes.
5. **Sealed vs final?**  
   `final` forbids all subclassing. `sealed` allows only the listed subtypes, which keeps extensibility but makes the set known to the compiler.
6. **Can a record extend a class?**  
   No. Records are implicitly `final` and can only implement interfaces.
7. **Why is `isAllowed` a `switch` expression without `default`?**  
   Enum switches that cover every constant are exhaustive, so adding a new status produces a compile error where it is not handled.
8. **What is a compact constructor?**  
   A record constructor without a parameter list. It runs validation and can reassign parameters before fields are assigned.
9. **`groupingBy` with `TreeMap::new`?**  
   The map supplier controls the result type. Here it gives sorted keys.
10. **Java 21 features used?**  
    Records, sealed interfaces, pattern matching for `switch`, and exhaustive switch expressions.
