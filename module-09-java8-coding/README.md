# Module 09 - Java 8 Coding Questions

Common coding questions solved with streams, solving them for an order-catalog domain (SKU codes, product names, and batches of order lines).

## Problems
1. **First non-repeated character** in a SKU code.
2. **Group anagrams:** product names that are anagrams of each other (e.g. "listen" / "silent").
3. **Second highest distinct price.**
4. **Chunk a list** into batches of N (e.g. for bulk shipping).
5. **Duplicate SKUs** found in an order batch.

## Interview questions
1. **Why is `Collectors.groupingBy` a good fit for anagrams?** Sort the characters to build a key, then group by it.
2. **How do you find the second highest distinct value?** `distinct()`, `sorted(reverseOrder())`, `skip(1)`, `findFirst()`.
3. **Why use `LinkedHashMap` for first non-repeated?** It keeps insertion order, so the first unique character is found in order.
4. **Stream reuse?** A stream can be consumed once. Create a new stream from the source for each query.

## Run
```bash
mvn test
```
