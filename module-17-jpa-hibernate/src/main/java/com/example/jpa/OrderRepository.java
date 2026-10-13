package com.example.jpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    /** Loads lines in the same query, avoiding the N+1 problem. */
    @EntityGraph(attributePaths = "lines")
    Optional<Order> findWithLinesById(Long id);
}
