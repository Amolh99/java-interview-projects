package com.example.employee;

import java.math.BigDecimal;

public record Employee(String name, String department, BigDecimal salary, int age) {}
