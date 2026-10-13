package com.example.java8;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class OrderAnalytics {

    private OrderAnalytics() {}

    public static Map<String, BigDecimal> revenueByCustomer(List<Order> orders) {
        return orders.stream().collect(Collectors.groupingBy(
                Order::customer,
                TreeMap::new,
                Collectors.reducing(BigDecimal.ZERO, Order::total, BigDecimal::add)));
    }

    public static List<String> topCustomers(List<Order> orders, int n) {
        return revenueByCustomer(orders).entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .limit(n)
                .map(Map.Entry::getKey)
                .toList();
    }

    public static Optional<Order> largestOrder(List<Order> orders) {
        return orders.stream().max(Comparator.comparing(Order::total));
    }

    public static Map<String, Long> countByCategory(List<Order> orders) {
        return orders.stream().collect(Collectors.groupingBy(
                Order::category, TreeMap::new, Collectors.counting()));
    }

    public static BigDecimal averageOrderValue(List<Order> orders) {
        if (orders.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal sum = orders.stream().map(Order::total).reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(BigDecimal.valueOf(orders.size()), 2, RoundingMode.HALF_UP);
    }

    /** Safe alternative to toMap without a merge function, which would throw on duplicate customers. */
    public static Map<String, BigDecimal> lastOrderTotalByCustomer(List<Order> orders) {
        return orders.stream().collect(Collectors.toMap(
                Order::customer,
                Order::total,
                (first, second) -> second,
                TreeMap::new));
    }

    public static Optional<String> customerEmail(Map<String, String> emails, String customer) {
        return Optional.ofNullable(emails.get(customer)).filter(e -> e.contains("@"));
    }

    public static List<String> allSkus(List<List<String>> skusPerOrder) {
        return skusPerOrder.stream()
                .flatMap(List::stream)
                .distinct()
                .sorted()
                .toList();
    }

    public static Map<String, Order> indexById(List<Order> orders) {
        return orders.stream().collect(Collectors.toMap(Order::id, Function.identity()));
    }
}
