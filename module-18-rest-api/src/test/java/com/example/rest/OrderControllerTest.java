package com.example.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@Import({OrderService.class, ApiExceptionHandler.class})
class OrderControllerTest {

    @Autowired MockMvc mvc;

    private static final String VALID = """
            {"customerId":"alice","items":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}]}""";

    @Test
    void createReturns201WithLocationHeader() throws Exception {
        mvc.perform(post("/api/v1/orders").contentType("application/json").content(VALID))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/v1/orders/")))
                .andExpect(jsonPath("$.total").value(25.00));
    }

    @Test
    void invalidBodyReturns400() throws Exception {
        mvc.perform(post("/api/v1/orders").contentType("application/json")
                        .content("{\"customerId\":\"\",\"items\":[]}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void unknownOrderReturns404() throws Exception {
        mvc.perform(get("/api/v1/orders/missing")).andExpect(status().isNotFound());
    }

    @Test
    void deleteReturns204ThenNotFound() throws Exception {
        String body = mvc.perform(post("/api/v1/orders").contentType("application/json").content(VALID))
                .andReturn().getResponse().getContentAsString();
        String id = body.replaceAll(".*\"id\":\"([^\"]+)\".*", "$1");
        mvc.perform(delete("/api/v1/orders/" + id)).andExpect(status().isNoContent());
        mvc.perform(delete("/api/v1/orders/" + id)).andExpect(status().isNotFound());
    }

    @Test
    void oversizedPageReturns400() throws Exception {
        mvc.perform(get("/api/v1/orders").param("size", "500"))
                .andExpect(status().isBadRequest());
    }
}
