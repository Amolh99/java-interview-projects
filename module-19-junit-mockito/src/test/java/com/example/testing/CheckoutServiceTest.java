package com.example.testing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

    @Mock PaymentGateway payments;
    @Mock OrderRepository orders;
    @InjectMocks CheckoutService service;

    @Test
    @DisplayName("charges the gateway, then saves the order with the transaction id")
    void happyPathChargesThenSaves() {
        when(payments.charge("alice", new BigDecimal("25.00"))).thenReturn("txn-42");

        String orderId = service.checkout("alice", new BigDecimal("25.00"));

        assertNotNull(orderId);
        InOrder order = inOrder(payments, orders);
        order.verify(payments).charge("alice", new BigDecimal("25.00"));
        order.verify(orders).save(orderId, "alice", "txn-42");
        verifyNoMoreInteractions(payments, orders);
    }

    @Test
    void declinedChargeIsNotSaved() {
        when(payments.charge(anyString(), any())).thenThrow(new IllegalStateException("declined"));

        assertThrows(IllegalStateException.class,
                () -> service.checkout("bob", BigDecimal.TEN));
        verifyNoInteractions(orders);
    }

    @Test
    void captorInspectsSavedOrder() {
        when(payments.charge(anyString(), any())).thenReturn("txn-7");

        String orderId = service.checkout("carol", BigDecimal.ONE);

        ArgumentCaptor<String> savedId = ArgumentCaptor.forClass(String.class);
        verify(orders).save(savedId.capture(), eq("carol"), eq("txn-7"));
        assertEquals(orderId, savedId.getValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-1.00"})
    void nonPositiveTotalIsRejectedBeforeCharging(String total) {
        assertThrows(IllegalArgumentException.class,
                () -> service.checkout("dan", new BigDecimal(total)));
        verifyNoInteractions(payments, orders);
    }

    @Test
    void nullTotalIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> service.checkout("eve", null));
    }
}
