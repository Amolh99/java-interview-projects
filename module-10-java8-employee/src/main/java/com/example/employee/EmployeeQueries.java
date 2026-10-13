package com.example.employee;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

public final class EmployeeQueries {

    private EmployeeQueries() {}

    public static Map<String, Double> averageSalaryByDepartment(List<Employee> staff) {
        return staff.stream().collect(Collectors.groupingBy(
                Employee::department,
                TreeMap::new,
                Collectors.averagingDouble(e -> e.salary().doubleValue())));
    }

    public static Map<String, Optional<Employee>> topEarnerByDepartment(List<Employee> staff) {
        return staff.stream().collect(Collectors.groupingBy(
                Employee::department,
                TreeMap::new,
                Collectors.maxBy(Comparator.comparing(Employee::salary))));
    }

    public static Map<String, BigDecimal> payrollByDepartment(List<Employee> staff) {
        return staff.stream().collect(Collectors.groupingBy(
                Employee::department,
                TreeMap::new,
                Collectors.reducing(BigDecimal.ZERO, Employee::salary, BigDecimal::add)));
    }

    public static List<String> namesOlderThan(List<Employee> staff, int age) {
        return staff.stream()
                .filter(e -> e.age() > age)
                .map(Employee::name)
                .sorted()
                .toList();
    }

    public static Optional<String> departmentWithHighestPayroll(List<Employee> staff) {
        return payrollByDepartment(staff).entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    public static Optional<BigDecimal> nthHighestSalary(List<Employee> staff, int n) {
        if (n < 1) {
            throw new IllegalArgumentException("n must be >= 1");
        }
        return staff.stream()
                .map(Employee::salary)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .skip(n - 1L)
                .findFirst();
    }

    public static BigDecimal applyRaise(BigDecimal salary, BigDecimal percent) {
        return salary.multiply(BigDecimal.ONE.add(percent.movePointLeft(2)))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
