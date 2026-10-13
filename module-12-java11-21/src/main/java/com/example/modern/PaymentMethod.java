package com.example.modern;

import java.math.BigDecimal;

/** Sealed hierarchy (Java 17+). The compiler knows every permitted payment method. */
public sealed interface PaymentMethod permits PaymentMethod.Card, PaymentMethod.BankTransfer, PaymentMethod.Wallet {

    record Card(String last4) implements PaymentMethod {}

    record BankTransfer(String iban) implements PaymentMethod {}

    record Wallet(String provider, BigDecimal balance) implements PaymentMethod {}
}
