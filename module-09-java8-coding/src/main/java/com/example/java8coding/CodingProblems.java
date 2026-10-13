package com.example.java8coding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class CodingProblems {

    private CodingProblems() {}

    /** First character that appears exactly once, in original order. */
    public static Optional<Character> firstNonRepeatedChar(String sku) {
        Map<Character, Long> counts = sku.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()));
        return counts.entrySet().stream()
                .filter(e -> e.getValue() == 1L)
                .map(Map.Entry::getKey)
                .findFirst();
    }

    /** Groups words that are anagrams. The sorted letters form the grouping key. */
    public static Collection<List<String>> groupAnagrams(List<String> words) {
        return words.stream().collect(Collectors.groupingBy(
                w -> w.chars().sorted().collect(StringBuilderCollector.INSTANCE),
                LinkedHashMap::new,
                Collectors.toList())).values();
    }

    /** Second highest distinct price, or empty if fewer than two distinct prices exist. */
    public static Optional<Integer> secondHighestPrice(List<Integer> prices) {
        return prices.stream()
                .distinct()
                .sorted(Comparator.reverseOrder())
                .skip(1)
                .findFirst();
    }

    /** Splits a list into consecutive batches of at most {@code size} elements. */
    public static <T> List<List<T>> chunk(List<T> items, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size must be > 0");
        }
        return IntStream.range(0, (items.size() + size - 1) / size)
                .mapToObj(i -> items.subList(i * size, Math.min(items.size(), (i + 1) * size)))
                .map(List::copyOf)
                .toList();
    }

    /** SKUs that appear more than once in a batch of order lines. */
    public static Set<String> duplicateSkus(List<String> skus) {
        return skus.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .filter(e -> e.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(java.util.TreeSet::new));
    }
}
