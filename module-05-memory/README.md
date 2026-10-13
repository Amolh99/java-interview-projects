# Module 05 - Java Memory Management

A listener registry for order events that avoids the classic memory leak: listeners that are never removed stay reachable forever.

## Concepts
- **Strong references:** keep objects alive. A `List<Listener>` never releases its entries.
- **Weak references:** do not prevent garbage collection. `WeakReference` is cleared once the referent is only weakly reachable.
- **Pruning:** the registry removes cleared references during notification.
- **Test note:** GC timing is not guaranteed. The GC test calls `System.gc()` in a bounded loop, so it is best-effort.

## Interview questions
1. **Strong, soft, weak, phantom?** Strong blocks GC. Soft is cleared under memory pressure. Weak is cleared at the next GC. Phantom runs after finalization.
2. **What is a memory leak in Java?** Objects stay reachable through references you no longer need, such as listeners or static caches.
3. **Does `System.gc()` guarantee collection?** No. It is a hint.
4. **Stack vs heap?** Local primitives and references are on the stack. Objects are on the heap.
5. **G1 vs ZGC?** G1 is the default and balances throughput and pauses. ZGC targets sub-millisecond pauses.
6. **What is a `WeakHashMap` used for?** Caches keyed by objects whose lifetime you don't control. Entries disappear when the key is collected.

## Run
```bash
mvn test
```
