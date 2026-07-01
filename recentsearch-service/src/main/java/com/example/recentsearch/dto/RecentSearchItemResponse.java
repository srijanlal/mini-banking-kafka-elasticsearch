package com.example.recentsearch.dto;

import java.time.Instant;

public record RecentSearchItemResponse(
        String id,
        String accountId,
        String accountNumber,
        String accountName,
        Instant clickedAt,
        String username
) {
}
