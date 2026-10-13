package com.example.reactive;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Map;

class OrderLookupServiceTest {

    private final CatalogClient client = new CatalogClient(
            Map.of(
                    "BOOK", new Product("BOOK", "Book", new BigDecimal("12.50")),
                    "PEN", new Product("PEN", "Pen", new BigDecimal("1.20"))),
            Map.of("BOOK", 4, "PEN", 0));

    private final OrderLookupService service = new OrderLookupService(client);

    @Test
    void quotesCombinePriceAndStock() {
        StepVerifier.create(service.quotes(List.of("BOOK", "PEN")))
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void stockFlagReflectsAvailability() {
        StepVerifier.create(service.orderedQuotes(List.of("BOOK", "PEN")))
                .assertNext(q -> {
                    assertEquals("BOOK", q.sku());
                    assertTrue(q.inStock());
                })
                .assertNext(q -> assertFalse(q.inStock()))
                .verifyComplete();
    }

    @Test
    void orderedQuotesPreserveInputOrder() {
        StepVerifier.create(service.orderedQuotes(List.of("PEN", "BOOK")))
                .assertNext(q -> assertEquals("PEN", q.sku()))
                .assertNext(q -> assertEquals("BOOK", q.sku()))
                .verifyComplete();
    }

    @Test
    void unknownSkuErrorsInStrictMode() {
        StepVerifier.create(service.quotes(List.of("NOPE")))
                .expectErrorMatches(e -> e instanceof IllegalArgumentException
                        && e.getMessage().contains("NOPE"))
                .verify();
    }

    @Test
    void unknownSkuUsesFallbackInLenientMode() {
        StepVerifier.create(service.quotesWithFallback(List.of("NOPE")))
                .assertNext(q -> {
                    assertFalse(q.inStock());
                    assertEquals(0, BigDecimal.ZERO.compareTo(q.price()));
                })
                .verifyComplete();
    }

    @Test
    void orderTotalSumsPrices() {
        StepVerifier.create(service.orderTotal(List.of("BOOK", "PEN")))
                .assertNext(total -> assertEquals(0, new BigDecimal("13.70").compareTo(total)))
                .verifyComplete();
    }

    @Test
    void publishersAreLazyUntilSubscribed() {
        Mono<Integer> stock = client.availableStock("BOOK");
        StepVerifier.create(stock).expectNext(4).verifyComplete();
        StepVerifier.create(Flux.just(1, 2, 3).take(Duration.ofSeconds(1)))
                .expectNextCount(3)
                .verifyComplete();
    }
}
