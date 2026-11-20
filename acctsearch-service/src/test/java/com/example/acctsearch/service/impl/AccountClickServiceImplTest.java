package com.example.acctsearch.service.impl;

import com.example.acctsearch.config.AcctSearchProperties;
import com.example.acctsearch.document.AccountDocument;
import com.example.acctsearch.dto.AccountClickResponse;
import com.example.acctsearch.exception.AccountNotFoundException;
import com.example.acctsearch.producer.AccountSearchEventProducer;
import com.example.acctsearch.repository.AccountDocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountClickServiceImplTest {

    @Mock
    private AccountDocumentRepository accountDocumentRepository;

    @Mock
    private AccountSearchEventProducer accountSearchEventProducer;

    @Mock
    private AcctSearchProperties properties;

    @InjectMocks
    private AccountClickServiceImpl service;

    @Test
    void registerClick_returnsAcceptedResponse_whenAccountExists() {
        // Arrange
        when(properties.defaultUsername()).thenReturn("system");
        AccountDocument doc = AccountDocument.builder()
                .id("acct-1")
                .accountNumber("ACC001")
                .accountName("Account One")
                .shortName("SN001")
                .officeCode("OFF01")
                .customerId("CUST01")
                .build();

        when(accountDocumentRepository.findById("acct-1")).thenReturn(Optional.of(doc));
        doNothing().when(accountSearchEventProducer).publish(any());

        // Act
        AccountClickResponse response = service.registerClick("acct-1");

        // Assert
        assertThat(response.accountId()).isEqualTo("acct-1");
        assertThat(response.status()).isEqualTo("ACCEPTED");
        assertThat(response.clickedAt()).isNotNull();
        assertThat(response.correlationId()).isNotNull();
        assertThat(response.message()).isNotBlank();
        verify(accountSearchEventProducer).publish(any());
    }

    @Test
    void registerClick_throwsAccountNotFoundException_whenAccountNotFound() {
        // Arrange
        when(accountDocumentRepository.findById("missing-id")).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> service.registerClick("missing-id"))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("missing-id");
    }
}
