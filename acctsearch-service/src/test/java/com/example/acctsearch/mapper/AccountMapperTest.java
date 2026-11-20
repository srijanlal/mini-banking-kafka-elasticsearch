package com.example.acctsearch.mapper;

import com.example.acctsearch.document.AccountDocument;
import com.example.acctsearch.dto.AccountResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountMapperTest {

    private final AccountMapper mapper = new AccountMapper();

    @Test
    void toResponse_mapsAllFields() {
        // Arrange
        AccountDocument doc = AccountDocument.builder()
                .id("id-1")
                .accountNumber("ACC001")
                .shortName("SN001")
                .accountName("Account One")
                .customerId("CUST001")
                .officeCode("OFF001")
                .status("ACTIVE")
                .build();

        // Act
        AccountResponse response = mapper.toResponse(doc);

        // Assert
        assertThat(response.id()).isEqualTo("id-1");
        assertThat(response.accountNumber()).isEqualTo("ACC001");
        assertThat(response.shortName()).isEqualTo("SN001");
        assertThat(response.accountName()).isEqualTo("Account One");
        assertThat(response.customerId()).isEqualTo("CUST001");
        assertThat(response.officeCode()).isEqualTo("OFF001");
        assertThat(response.status()).isEqualTo("ACTIVE");
    }

    @Test
    void toResponse_nullFieldsPreserved() {
        // Arrange
        AccountDocument doc = AccountDocument.builder().id("id-2").build();

        // Act
        AccountResponse response = mapper.toResponse(doc);

        // Assert
        assertThat(response.id()).isEqualTo("id-2");
        assertThat(response.accountNumber()).isNull();
        assertThat(response.status()).isNull();
    }
}
