# Module 12 - Java 11 to 21 Features

Order-domain code that uses the language features added between Java 11 and 21.

## Features covered
| Version | Feature | Where |
|---|---|---|
| 11 | `var` in lambdas, `String.isBlank`, `strip`, `lines` | `OrderText` |
| 14 | Switch expressions, arrow labels | `ShippingRules` |
| 16 | Records | `OrderLine` |
| 17 | Sealed classes | `PaymentMethod` |
| 21 | Pattern matching for `switch`, record patterns | `PaymentFeeCalculator` |
| 21 | Sequenced collections (`getFirst`, `reversed`) | `OrderHistory` |
| 21 | Virtual threads | `VirtualThreadBatch` |

## Interview questions
1. **Why are records useful?** They are immutable carriers with generated `equals`, `hashCode`, and `toString`.
2. **Sealed vs final?** Sealed lists the permitted subtypes, so the compiler can check `switch` exhaustiveness.
3. **Pattern matching `switch` vs `if/else instanceof`?** It is exhaustive for sealed types and avoids casts.
4. **Virtual threads vs platform threads?** Virtual threads are cheap and suit blocking I/O. They do not make CPU work faster.
5. **What is a sequenced collection?** An interface (`SequencedCollection`) with `getFirst`, `getLast`, and `reversed()`.

## Run
```bash
mvn test
```
