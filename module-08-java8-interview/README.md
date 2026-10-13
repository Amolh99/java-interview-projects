# Module 08 - Java 8 Interview Questions

Order analytics written with lambdas, streams, `Optional`, method references, and default methods. Each method maps to a common interview question.

## Concepts
- **Lambdas and functional interfaces:** `Discountable.discounted` shows a default method on an interface.
- **Streams:** `groupingBy`, `reducing`, `sorted`, `limit`, `flatMap`.
- **Optional:** `largestOrder` and `customerEmail` return `Optional` instead of `null`.
- **Laziness:** intermediate operations do nothing until a terminal operation runs.
- **Collector pitfall:** `Collectors.toMap` without a merge function throws on duplicate keys.

## Interview questions
1. **What is a functional interface?** An interface with one abstract method, such as `Function` or `Predicate`.
2. **Intermediate vs terminal operations?** Intermediate ones are lazy; terminal ones trigger the pipeline.
3. **`map` vs `flatMap`?** `flatMap` flattens nested streams, e.g. `List<List<String>>` to `List<String>`.
4. **Why is `Optional` not for fields or parameters?** It is meant as a return type. It is not serializable and adds allocation.
5. **`Collectors.toMap` duplicate key?** It throws `IllegalStateException` unless you pass a merge function.
6. **Default methods?** Let interfaces evolve without breaking implementers. They also allow diamond conflicts that must be resolved.
7. **Can a stream be reused?** No. It can be consumed only once.
8. **`parallelStream` always faster?** No. It has overhead and suits CPU-bound work on large data.

## Run
```bash
mvn test
```
