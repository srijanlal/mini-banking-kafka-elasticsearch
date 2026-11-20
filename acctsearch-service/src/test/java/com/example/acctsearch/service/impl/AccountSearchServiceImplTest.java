package com.example.acctsearch.service.impl;

import com.example.acctsearch.config.AcctSearchProperties;
import com.example.acctsearch.document.AccountDocument;
import com.example.acctsearch.dto.AccountResponse;
import com.example.acctsearch.dto.AccountSearchResponse;
import com.example.acctsearch.mapper.AccountMapper;
import com.example.acctsearch.repository.AccountDocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountSearchServiceImplTest {

    @Mock
    private AccountDocumentRepository accountDocumentRepository;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private AcctSearchProperties properties;

    @InjectMocks
    private AccountSearchServiceImpl service;

    @BeforeEach
    void setUp() {
        when(properties.maxResults()).thenReturn(10);
    }

    @Test
    void search_returnsMatchingAccounts() {
        // Arrange
        AccountDocument doc = AccountDocument.builder().id("1").accountNumber("ACC001").build();
        AccountResponse response = new AccountResponse("1", "ACC001", null, null, null, null, null);
        Page<AccountDocument> page = new PageImpl<>(List.of(doc));

        when(accountDocumentRepository
                .findByAccountNumberContainingIgnoreCaseOrShortNameContainingIgnoreCaseOrAccountNameContainingIgnoreCaseOrCustomerIdContainingIgnoreCaseOrOfficeCodeContainingIgnoreCase(
                        eq("ACC001"), eq("ACC001"), eq("ACC001"), eq("ACC001"), eq("ACC001"),
                        any(PageRequest.class)))
                .thenReturn(page);
        when(accountMapper.toResponse(doc)).thenReturn(response);

        // Act
        AccountSearchResponse result = service.search("ACC001");

        // Assert
        assertThat(result.accounts()).hasSize(1);
        assertThat(result.count()).isEqualTo(1);
        assertThat(result.accounts().get(0).accountNumber()).isEqualTo("ACC001");
    }

    @Test
    void search_trimsQueryBeforeSearching() {
        // Arrange — query with leading/trailing spaces; expect trimmed value to be used
        Page<AccountDocument> emptyPage = new PageImpl<>(List.of());

        when(accountDocumentRepository
                .findByAccountNumberContainingIgnoreCaseOrShortNameContainingIgnoreCaseOrAccountNameContainingIgnoreCaseOrCustomerIdContainingIgnoreCaseOrOfficeCodeContainingIgnoreCase(
                        eq("ACC"), eq("ACC"), eq("ACC"), eq("ACC"), eq("ACC"),
                        any(PageRequest.class)))
                .thenReturn(emptyPage);

        // Act
        AccountSearchResponse result = service.search("  ACC  ");

        // Assert
        assertThat(result.accounts()).isEmpty();
        assertThat(result.count()).isZero();
    }

    @Test
    void search_returnsEmptyWhenNoResults() {
        // Arrange
        Page<AccountDocument> emptyPage = new PageImpl<>(List.of());

        when(accountDocumentRepository
                .findByAccountNumberContainingIgnoreCaseOrShortNameContainingIgnoreCaseOrAccountNameContainingIgnoreCaseOrCustomerIdContainingIgnoreCaseOrOfficeCodeContainingIgnoreCase(
                        any(), any(), any(), any(), any(), any(PageRequest.class)))
                .thenReturn(emptyPage);

        // Act
        AccountSearchResponse result = service.search("unknown");

        // Assert
        assertThat(result.accounts()).isEmpty();
        assertThat(result.count()).isZero();
    }
}
