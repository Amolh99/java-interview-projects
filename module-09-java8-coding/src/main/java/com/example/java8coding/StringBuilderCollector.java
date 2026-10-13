package com.example.java8coding;

import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.Set;

/** Collects an IntStream of chars into a sorted String, used as an anagram key. */
final class StringBuilderCollector {

    static final Collector<Integer, StringBuilder, String> INSTANCE = Collector.of(
            StringBuilder::new,
            (StringBuilder sb, Integer c) -> sb.append((char) c.intValue()),
            (a, b) -> a.append(b),
            StringBuilder::toString);

    private StringBuilderCollector() {}
}
