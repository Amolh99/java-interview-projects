package com.example.k8s;

public class OrderServiceMain {

    public static void main(String[] args) {
        HealthStatus health = new HealthStatus(true, true);
        System.out.println("live=" + health.live() + " ready=" + health.ready());
    }
}
