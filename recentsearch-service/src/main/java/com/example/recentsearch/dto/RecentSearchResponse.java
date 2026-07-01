package com.example.recentsearch.dto;

import java.util.List;

public record RecentSearchResponse(
        List<RecentSearchItemResponse> items,
        int count
) {
}
