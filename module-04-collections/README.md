# Module 04 - Java Collections

Collections used in an order service: an LRU product cache, a shipping priority queue, and a `Product` class with a correct `equals`/`hashCode` contract.

## Concepts
- **LRU cache:** `LinkedHashMap` with `accessOrder=true` and `removeEldestEntry`.
- **Priority queue:** `PriorityQueue` is a binary heap. It gives the head in O(log n), but iteration order is not sorted.
- **equals/hashCode contract:** equal objects must have equal hash codes. Mutable hash keys break `HashMap` lookups.

## Interview questions
1. **HashMap vs LinkedHashMap vs TreeMap?** Hash lookup, insertion order, and sorted order respectively.
2. **Why does `PriorityQueue` iteration look unsorted?** It only guarantees the head is the minimum. Use `poll()` to get sorted output.
3. **What breaks if you override `equals` but not `hashCode`?** `HashSet` and `HashMap` treat equal objects as different.
4. **ArrayList vs LinkedList?** ArrayList is faster for random access and most real workloads. LinkedList is rarely the right choice.
5. **Fail-fast iterators?** Modifying a collection during for-each throws `ConcurrentModificationException`.
6. **Why use `List.of` / `Map.of`?** They are immutable and reject nulls.

## Run
```bash
mvn test
```
