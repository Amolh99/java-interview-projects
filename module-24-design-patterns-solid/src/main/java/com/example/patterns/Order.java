package com.example.patterns;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/** Immutable order built with the Builder pattern. Validation runs once, in build(). */
public final class Order {

    private final String id;
    private final String customerId;
    private final List<BigDecimal> lines;
    private final String note;

    private Order(Builder b) {
        this.id = b.id;
        this.customerId = b.customerId;
        this.lines = List.copyOf(b.lines);
        this.note = b.note;
    }

    public String id() { return id; }
    public String customerId() { return customerId; }
    public List<BigDecimal> lines() { return lines; }
    public String note() { return note; }

    public static Builder builder(String id, String customerId) {
        return new Builder(id, customerId);
    }

    public static final class Builder {
        private final String id;
        private final String customerId;
        private final java.util.ArrayList<BigDecimal> lines = new java.util.ArrayList<>();
        private String note = "";

        private Builder(String id, String customerId) {
            this.id = Objects.requireNonNull(id);
            this.customerId = Objects.requireNonNull(customerId);
        }

        public Builder addLine(BigDecimal price) {
            if (price.signum() < 0) {
                throw new IllegalArgumentException("price must be >= 0");
            }
            lines.add(price);
            return this;
        }

        public Builder note(String note) {
            this.note = Objects.requireNonNullElse(note, "");
            return this;
        }

        public Order build() {
            if (lines.isEmpty()) {
                throw new IllegalStateException("order needs at least one line");
            }
            return new Order(this);
        }
    }
}
