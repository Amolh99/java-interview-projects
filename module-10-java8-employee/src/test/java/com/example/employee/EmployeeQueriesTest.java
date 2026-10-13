package com.example.employee;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeQueriesTest {

    private static final List<Employee> STAFF = List.of(
            new Employee("Asha", "Picking", new BigDecimal("3000"), 34),
            new Employee("Ben", "Picking", new BigDecimal("3600"), 45),
            new Employee("Chen", "Shipping", new BigDecimal("4000"), 29),
            new Employee("Dara", "Shipping", new BigDecimal("4000"), 51),
            new Employee("Eli", "Returns", new BigDecimal("2500"), 38));

    @Test
    void averageSalaryPerDepartment() {
        var avg = EmployeeQueries.averageSalaryByDepartment(STAFF);
        assertEquals(3300.0, avg.get("Picking"), 0.001);
        assertEquals(4000.0, avg.get("Shipping"), 0.001);
    }

    @Test
    void topEarnerIsOptionalPerDepartment() {
        var top = EmployeeQueries.topEarnerByDepartment(STAFF);
        assertEquals("Ben", top.get("Picking").orElseThrow().name());
        assertEquals("Eli", top.get("Returns").orElseThrow().name());
    }

    @Test
    void payrollPerDepartmentUsesBigDecimal() {
        var payroll = EmployeeQueries.payrollByDepartment(STAFF);
        assertEquals(0, new BigDecimal("6600").compareTo(payroll.get("Picking")));
        assertEquals(0, new BigDecimal("8000").compareTo(payroll.get("Shipping")));
    }

    @Test
    void namesOlderThanAreSorted() {
        assertEquals(List.of("Ben", "Dara", "Eli"), EmployeeQueries.namesOlderThan(STAFF, 37));
    }

    @Test
    void departmentWithHighestPayrollIsShipping() {
        assertEquals(Optional.of("Shipping"), EmployeeQueries.departmentWithHighestPayroll(STAFF));
    }

    @Test
    void nthHighestSalaryIgnoresDuplicates() {
        assertEquals(0, new BigDecimal("3600").compareTo(
                EmployeeQueries.nthHighestSalary(STAFF, 2).orElseThrow()));
        assertTrue(EmployeeQueries.nthHighestSalary(STAFF, 10).isEmpty());
    }

    @Test
    void raiseIsAppliedWithHalfUpRounding() {
        assertEquals(0, new BigDecimal("3300.00").compareTo(
                EmployeeQueries.applyRaise(new BigDecimal("3000"), new BigDecimal("10"))));
    }
}
