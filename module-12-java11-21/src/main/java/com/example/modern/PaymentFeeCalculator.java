package com.example.modern;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Java 21 pattern matching for switch, including record deconstruction patterns. */
public final class PaymentFeeCalculator {

    private PaymentFeeCalculator() {}

    public static BigDecimal fee(PaymentMethod method, BigDecimal amount) {
        return switch (method) {
            case PaymentMethod.Card(String last4) when last4.length() == 4 ->
                    amount.multiply(new BigDecimal("0.029")).setScale(2, RoundingMode.HALF_UP);
            case PaymentMethod.Card c -> throw new IllegalArgumentException("invalid card reference");
            case PaymentMethod.BankTransfer b -> BigDecimal.ZERO;
            case PaymentMethod.Wallet(String provider, BigDecimal balance) ->
                    amount.compareTo(balance) > 0
                            ? new BigDecimal("0.50")
                            : BigDecimal.ZERO;
        };
    }
}
