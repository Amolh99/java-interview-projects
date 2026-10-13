package com.example.resilience;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/** Cache-aside. computeIfAbsent makes concurrent misses for the same key load only once. */
public class CacheAside<K, V> {

    private final Map<K, V> cache = new ConcurrentHashMap<>();
    private final Function<K, V> loader;

    public CacheAside(Function<K, V> loader) {
        this.loader = loader;
    }

    public V get(K key) {
        return cache.computeIfAbsent(key, loader);
    }

    public void evict(K key) {
        cache.remove(key);
    }

    public int size() {
        return cache.size();
    }
}
