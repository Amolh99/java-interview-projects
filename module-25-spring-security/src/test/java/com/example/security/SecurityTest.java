package com.example.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityTest {

    @Autowired MockMvc mvc;

    @Test
    void publicEndpointNeedsNoLogin() throws Exception {
        mvc.perform(get("/api/public/status")).andExpect(status().isOk());
    }

    @Test
    void protectedEndpointRejectsAnonymous() throws Exception {
        mvc.perform(get("/api/admin/reports")).andExpect(status().isUnauthorized());
    }

    @Test
    void customerCannotReachAdminArea() throws Exception {
        mvc.perform(get("/api/admin/reports").with(httpBasic("alice", "alice-pass")))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminCanReachAdminArea() throws Exception {
        mvc.perform(get("/api/admin/reports").with(httpBasic("admin", "admin-pass")))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "alice", roles = "CUSTOMER")
    void customerCanReadOwnOrders() throws Exception {
        mvc.perform(get("/api/customers/alice/orders")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "alice", roles = "CUSTOMER")
    void customerCannotReadAnotherCustomersOrders() throws Exception {
        mvc.perform(get("/api/customers/bob/orders")).andExpect(status().isForbidden());
    }
}
