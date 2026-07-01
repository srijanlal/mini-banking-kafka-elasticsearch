package com.example.acctsearch.service.impl;

import com.example.acctsearch.config.AcctSearchProperties;
import com.example.acctsearch.document.AccountDocument;
import com.example.acctsearch.dto.AccountResponse;
import com.example.acctsearch.dto.AccountSearchResponse;
import com.example.acctsearch.mapper.AccountMapper;
import com.example.acctsearch.repository.AccountDocumentRepository;
import com.example.acctsearch.service.AccountSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountSearchServiceImpl implements AccountSearchService {

    private final AccountDocumentRepository accountDocumentRepository;
    private final AccountMapper accountMapper;
    private final AcctSearchProperties properties;

    @Override
    public AccountSearchResponse search(String query) {
    String normalizedQuery = query.trim();

    Page<AccountDocument> page = accountDocumentRepository
        .findByAccountNumberContainingIgnoreCaseOrShortNameContainingIgnoreCaseOrAccountNameContainingIgnoreCaseOrCustomerIdContainingIgnoreCaseOrOfficeCodeContainingIgnoreCase(
            normalizedQuery,
            normalizedQuery,
            normalizedQuery,
            normalizedQuery,
            normalizedQuery,
            PageRequest.of(0, properties.maxResults())
        );

    List<AccountResponse> matches = page.getContent().stream()
        .map(accountMapper::toResponse)
                .toList();

        log.info("Account search query='{}' returned {} results", normalizedQuery, matches.size());
        return new AccountSearchResponse(matches, matches.size());
    }
}
