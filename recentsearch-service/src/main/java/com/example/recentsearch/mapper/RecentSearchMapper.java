package com.example.recentsearch.mapper;

import com.example.recentsearch.document.RecentSearchDocument;
import com.example.recentsearch.dto.RecentSearchItemResponse;
import org.springframework.stereotype.Component;

@Component
public class RecentSearchMapper {

    public RecentSearchItemResponse toResponse(RecentSearchDocument document) {
        return new RecentSearchItemResponse(
                document.getId(),
                document.getAccountId(),
                document.getAccountNumber(),
                document.getAccountName(),
                document.getClickedAt(),
                document.getUsername()
        );
    }
}
