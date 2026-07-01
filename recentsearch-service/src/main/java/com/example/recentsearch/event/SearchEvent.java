package com.example.recentsearch.event;

import java.time.Instant;

public record SearchEvent(
        String accountId,
        String accountNumber,
        String accountName,
        String shortName,
        String officeCode,
        String customerId,
        Instant clickedAt,
        String username,
        String correlationId
) {
}
