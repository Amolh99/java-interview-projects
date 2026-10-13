package com.example.functional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class FunctionalTools {

    private FunctionalTools() {}

    public static UnaryOperator<BigDecimal> percentOff(BigDecimal percent) {
        return price -> price.multiply(BigDecimal.ONE.subtract(percent.movePointLeft(2)))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public static UnaryOperator<BigDecimal> flatFee(BigDecimal fee) {
        return price -> price.add(fee);
    }

    /** Chains pricing rules left to right: first rule runs first. */
    public static Function<BigDecimal, BigDecimal> pipeline(List<UnaryOperator<BigDecimal>> rules) {
        Function<BigDecimal, BigDecimal> result = Function.identity();
        for (UnaryOperator<BigDecimal> rule : rules) {
            result = result.andThen(rule);
        }
        return result;
    }

    public static <T> Predicate<T> allOf(List<Predicate<T>> rules) {
        Predicate<T> result = t -> true;
        for (Predicate<T> rule : rules) {
            result = result.and(rule);
        }
        return result;
    }

    public static <T, R> Function<T, R> memoize(Function<T, R> function) {
        Map<T, R> cache = new ConcurrentHashMap<>();
        return input -> cache.computeIfAbsent(input, function);
    }

    /** Calls the supplier up to {@code attempts} times, returning the first success. Rethrows the last failure. */
    public static <T> T retry(Supplier<T> action, int attempts) {
        if (attempts < 1) {
            throw new IllegalArgumentException("attempts must be >= 1");
        }
        List<RuntimeException> failures = new ArrayList<>();
        for (int i = 0; i < attempts; i++) {
            try {
                return action.get();
            } catch (RuntimeException e) {
                failures.add(e);
            }
        }
        RuntimeException last = failures.get(failures.size() - 1);
        failures.subList(0, failures.size() - 1).forEach(last::addSuppressed);
        throw last;
    }

    /** Curried addition: add(2).apply(3) == 5. */
    public static Function<Integer, Function<Integer, Integer>> curriedAdd() {
        return a -> b -> a + b;
    }
}
