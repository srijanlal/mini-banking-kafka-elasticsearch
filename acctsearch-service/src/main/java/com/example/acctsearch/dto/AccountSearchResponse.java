package com.example.acctsearch.dto;

import java.util.List;

public record AccountSearchResponse(
        List<AccountResponse> accounts,
        int count
) {
}
