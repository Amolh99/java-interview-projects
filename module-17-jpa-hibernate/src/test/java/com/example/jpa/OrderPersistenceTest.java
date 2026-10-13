package com.example.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

/** Runs against an embedded H2 database, with each test rolled back afterwards. */
@DataJpaTest
class OrderPersistenceTest {

    @Autowired OrderService service;
    @Autowired OrderRepository repository;

    @Test
    void placedOrderIsPersistedWithItsLines() {
        Long id = service.placeOrder("alice", "BOOK", "PEN");
        Order loaded = repository.findWithLinesById(id).orElseThrow();
        assertEquals("alice", loaded.getCustomerId());
        assertEquals(2, loaded.getLines().size());
    }

    @Test
    void orphanRemovalDeletesLines() {
        Long id = service.placeOrder("bob", "LAMP");
        service.removeAllLines(id);
        Order loaded = repository.findWithLinesById(id).orElseThrow();
        assertTrue(loaded.getLines().isEmpty());
    }

    @Test
    void emptyOrderIsRejectedAndNothingIsSaved() {
        long before = repository.count();
        assertThrows(IllegalArgumentException.class, () -> service.placeOrder("carol"));
        assertEquals(before, repository.count());
    }
}
