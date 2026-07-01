package com.example.recentsearch.service.impl;

import com.example.recentsearch.config.RecentSearchProperties;
import com.example.recentsearch.document.RecentSearchDocument;
import com.example.recentsearch.dto.RecentSearchItemResponse;
import com.example.recentsearch.dto.RecentSearchResponse;
import com.example.recentsearch.event.SearchEvent;
import com.example.recentsearch.mapper.RecentSearchMapper;
import com.example.recentsearch.repository.RecentSearchDocumentRepository;
import com.example.recentsearch.service.RecentSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecentSearchServiceImpl implements RecentSearchService {

    private final RecentSearchDocumentRepository repository;
    private final RecentSearchMapper mapper;
    private final RecentSearchProperties properties;

    @Override
    public RecentSearchResponse getRecentSearches(int limit) {
        int resolvedLimit = Math.min(limit, properties.maxResults());
        Page<RecentSearchDocument> page = repository.findAllByOrderByClickedAtDesc(PageRequest.of(0, resolvedLimit));

        List<RecentSearchItemResponse> items = page.getContent().stream()
                .map(mapper::toResponse)
                .toList();

        log.info("Returning {} recent searches", items.size());
        return new RecentSearchResponse(items, items.size());
    }

    @Override
    public void saveFromEvent(SearchEvent event) {
        if (event.accountId() == null || event.accountId().isBlank()) {
            throw new IllegalArgumentException("accountId is required in SearchEvent");
        }

        RecentSearchDocument document = RecentSearchDocument.builder()
                .id(resolveDocumentId(event))
                .accountId(event.accountId())
                .accountNumber(event.accountNumber())
                .accountName(event.accountName())
                .clickedAt(event.clickedAt() == null ? Instant.now() : event.clickedAt())
                .username(event.username())
                .build();

        repository.save(document);
        log.info("Stored recent search accountId='{}', correlationId='{}'", event.accountId(), event.correlationId());
    }

    private String resolveDocumentId(SearchEvent event) {
        if (event.correlationId() != null && !event.correlationId().isBlank()) {
            return event.correlationId();
        }
        return UUID.randomUUID().toString();
    }
}
