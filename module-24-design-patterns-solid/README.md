# Module 24 - Design Patterns & SOLID

The same order domain built with five patterns. Each pattern solves a problem that the code otherwise repeats.

## Patterns
| Pattern | Class | Problem solved |
|---|---|---|
| Builder | `Order.Builder` | Many optional fields, validated once at `build()` |
| Observer | `OrderEvents` | Notify inventory, email, and analytics without the order knowing them |
| Strategy | `ShippingPolicy` | Swap shipping rules without `if/else` chains |
| Decorator | `DiscountedPricing` | Stack discounts without subclass explosion |
| Factory Method | `ShippingPolicies.forRegion` | Hide which strategy a region gets |

## SOLID mapping
- **S:** `OrderTotals` only calculates. Persistence and notification live elsewhere.
- **O:** adding a shipping policy means a new class, not an edited one.
- **L:** any `Pricing` can replace another, so decorators keep the contract.
- **I:** `Pricing` has one method. Clients do not depend on methods they don't use.
- **D:** `OrderCheckout` depends on the `Pricing` and `ShippingPolicy` interfaces.

## Interview questions
1. **Observer vs pub/sub?** Observer is in-process and synchronous. Pub/sub goes through a broker, usually asynchronous.
2. **Decorator vs inheritance?** Decorators compose at runtime. Inheritance is fixed at compile time.
3. **When is Builder overkill?** With two or three required fields, a constructor is clearer.
4. **Strategy vs State?** Strategy is chosen by the client. State changes itself as the object moves through states.
5. **Which SOLID principle does a `switch` on type break?** Open/Closed: adding a case edits existing code.

## Run
```bash
mvn test
```
