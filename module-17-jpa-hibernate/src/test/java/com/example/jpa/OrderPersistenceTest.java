package com.example.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/** Loads the full context so OrderService is wired. Each test is rolled back afterwards. */
@SpringBootTest
@Transactional
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
