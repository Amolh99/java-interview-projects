package com.example.modern;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ModernJavaTest {

    @Test
    void java11StringHelpers() {
        assertTrue(OrderText.isMissing("   "));
        assertTrue(OrderText.isMissing(null));
        assertEquals("gift wrap", OrderText.normalise("  gift wrap "));
        assertEquals(2, OrderText.lineCount("a\nb"));
    }

    @Test
    void recordValidatesInCompactConstructor() {
        assertThrows(IllegalArgumentException.class,
                () -> new OrderLine("A", 0, BigDecimal.ONE));
        assertEquals(0, new BigDecimal("7.50").compareTo(
                new OrderLine("A", 3, new BigDecimal("2.50")).lineTotal()));
    }

    @Test
    void switchExpressionWithYield() {
        assertEquals(1, ShippingRules.deliveryDays("LOCAL", DayOfWeek.MONDAY));
        assertEquals(4, ShippingRules.deliveryDays("NATIONAL", DayOfWeek.SUNDAY));
        assertEquals(9, ShippingRules.deliveryDays("INTERNATIONAL", DayOfWeek.FRIDAY));
        assertThrows(IllegalArgumentException.class,
                () -> ShippingRules.deliveryDays("MARS", DayOfWeek.MONDAY));
    }

    @Test
    void patternMatchingOverSealedPaymentMethods() {
        BigDecimal amount = new BigDecimal("100.00");
        assertEquals(0, new BigDecimal("2.90").compareTo(
                PaymentFeeCalculator.fee(new PaymentMethod.Card("4242"), amount)));
        assertEquals(0, BigDecimal.ZERO.compareTo(
                PaymentFeeCalculator.fee(new PaymentMethod.BankTransfer("DE00"), amount)));
        assertEquals(0, new BigDecimal("0.50").compareTo(
                PaymentFeeCalculator.fee(new PaymentMethod.Wallet("pay", BigDecimal.TEN), amount)));
    }

    @Test
    void sequencedCollectionsExposeEndsAndReverse() {
        OrderHistory history = new OrderHistory();
        history.record("placed");
        history.record("paid");
        history.record("shipped");
        assertEquals("shipped", history.latest());
        assertEquals("placed", history.earliest());
        assertEquals(List.of("shipped", "paid", "placed"), history.newestFirst());
    }

    @Test
    void virtualThreadsReturnResultsInOrder() throws Exception {
        assertEquals(List.of("status:o1", "status:o2"),
                VirtualThreadBatch.fetchAll(List.of("o1", "o2")));
    }
}
