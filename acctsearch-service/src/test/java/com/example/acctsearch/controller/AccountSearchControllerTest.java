package com.example.acctsearch.controller;

import com.example.acctsearch.dto.AccountClickResponse;
import com.example.acctsearch.dto.AccountResponse;
import com.example.acctsearch.dto.AccountSearchResponse;
import com.example.acctsearch.exception.AccountNotFoundException;
import com.example.acctsearch.service.AccountClickService;
import com.example.acctsearch.service.AccountSearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountSearchController.class)
class AccountSearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountSearchService accountSearchService;

    @MockBean
    private AccountClickService accountClickService;

    @Test
    void searchAccounts_returns200WithResults() throws Exception {
        // Arrange
        AccountResponse acct = new AccountResponse("1", "ACC001", "SN", "Name", "CUST", "OFF", "ACTIVE");
        AccountSearchResponse searchResponse = new AccountSearchResponse(List.of(acct), 1);
        when(accountSearchService.search("ACC")).thenReturn(searchResponse);

        // Act + Assert
        mockMvc.perform(get("/api/accounts/search").param("query", "ACC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.accounts[0].accountNumber").value("ACC001"));
    }

    @Test
    void searchAccounts_returns400WhenQueryIsBlank() throws Exception {
        mockMvc.perform(get("/api/accounts/search").param("query", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void searchAccounts_returns400WhenQueryIsTooShort() throws Exception {
        // @Size(min=2) — single character should fail
        mockMvc.perform(get("/api/accounts/search").param("query", "A"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void clickAccount_returns200() throws Exception {
        // Arrange
        AccountClickResponse clickResponse = new AccountClickResponse(
                "acct-1", "ACCEPTED", Instant.now(), "corr-1", "Click captured and published to Kafka.");
        when(accountClickService.registerClick("acct-1")).thenReturn(clickResponse);

        // Act + Assert
        mockMvc.perform(post("/api/accounts/acct-1/click"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACCEPTED"))
                .andExpect(jsonPath("$.accountId").value("acct-1"));
    }

    @Test
    void clickAccount_returns404WhenAccountNotFound() throws Exception {
        // Arrange
        when(accountClickService.registerClick(anyString()))
                .thenThrow(new AccountNotFoundException("missing-id"));

        // Act + Assert
        mockMvc.perform(post("/api/accounts/missing-id/click"))
                .andExpect(status().isNotFound());
    }
}
