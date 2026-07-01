package com.example.acctsearch.dto;

public record AccountResponse(
        String id,
        String accountNumber,
        String shortName,
        String accountName,
        String customerId,
        String officeCode,
        String status
) {
}
