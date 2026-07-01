package com.example.acctsearch.dto;

import java.time.Instant;

public record AccountClickResponse(
        String accountId,
        String status,
        Instant clickedAt,
        String correlationId,
        String message
) {
}
