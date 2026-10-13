# Module 03 - JVM Internals

Small tools that inspect the JVM at runtime for an order service: heap usage, GC collectors, and class loading.

## Concepts
- **Heap and GC:** `MemoryMXBean` and `GarbageCollectorMXBean` expose runtime metrics.
- **Class loading:** bootstrap, platform, and application loaders. Parent delegation means core classes such as `String` always come from the bootstrap loader.
- **Runtime version:** `Runtime.version()` reports the Java feature release.

## Interview questions
1. **What is parent delegation?** A class loader asks its parent first. This stops user code from replacing `java.lang.String`.
2. **Which loader loads `String`?** The bootstrap loader. `String.class.getClassLoader()` returns `null`.
3. **What are the main GC families?** Serial, Parallel, G1 (default), ZGC, and Shenandoah.
4. **Heap vs metaspace?** Objects live on the heap. Class metadata lives in metaspace, outside the heap, since Java 8.
5. **JIT vs interpreter?** The interpreter starts fast. The JIT compiles hot methods to native code for speed.

## Run
```bash
mvn test
```
