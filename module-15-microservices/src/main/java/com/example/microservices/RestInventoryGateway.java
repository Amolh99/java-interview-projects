package com.example.microservices;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/** Adapter: calls the inventory service over HTTP. The base URL comes from configuration. */
@Component
public class RestInventoryGateway implements InventoryGateway {

    private final RestClient client;

    public RestInventoryGateway(@Value("${inventory.base-url:http://localhost:8081}") String baseUrl) {
        this.client = RestClient.builder().baseUrl(baseUrl).build();
    }

    @Override
    public boolean isAvailable(String sku, int quantity) {
        Availability body = client.get()
                .uri(uriBuilder -> uriBuilder.path("/api/inventory/{sku}")
                        .queryParam("quantity", quantity)
                        .build(sku))
                .retrieve()
                .body(Availability.class);
        return body != null && body.available();
    }

    public record Availability(String sku, boolean available) {}
}
