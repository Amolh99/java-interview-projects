# Module 10 - Java 8 Employee Questions

Classic employee-data questions, using the staff of a warehouse that fulfils orders. Each method is a typical interview prompt.

## Problems
1. Average salary per department.
2. Top earner per department (`Optional` per group).
3. Total payroll per department.
4. Names of employees older than N, sorted.
5. Department with the highest payroll.
6. Salary raise by percentage, using `BigDecimal`.

## Interview questions
1. **`averagingDouble` vs `averagingInt`?** Both return `Double`, so compare with a delta. For money, use `BigDecimal` reduction.
2. **How do you find the max per group?** `groupingBy` with `maxBy(Comparator)`, which returns `Optional`.
3. **Why `TreeMap` as the map supplier?** Sorted department names in the result.
4. **Nth highest salary?** Distinct, sorted descending, `skip(n-1)`, `findFirst`.
5. **`reduce` identity?** Must be neutral for the operation (`BigDecimal.ZERO` for addition).

## Run
```bash
mvn test
```
