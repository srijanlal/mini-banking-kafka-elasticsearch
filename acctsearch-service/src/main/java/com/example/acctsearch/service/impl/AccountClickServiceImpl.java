package com.example.acctsearch.service.impl;

import com.example.acctsearch.config.AcctSearchProperties;
import com.example.acctsearch.document.AccountDocument;
import com.example.acctsearch.dto.AccountClickResponse;
import com.example.acctsearch.event.SearchEvent;
import com.example.acctsearch.exception.AccountNotFoundException;
import com.example.acctsearch.producer.AccountSearchEventProducer;
import com.example.acctsearch.repository.AccountDocumentRepository;
import com.example.acctsearch.service.AccountClickService;
import com.example.acctsearch.util.CorrelationIdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountClickServiceImpl implements AccountClickService {

    private final AccountDocumentRepository accountDocumentRepository;
    private final AccountSearchEventProducer accountSearchEventProducer;
    private final AcctSearchProperties properties;

    @Override
    public AccountClickResponse registerClick(String accountId) {
        AccountDocument account = accountDocumentRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        Instant clickedAt = Instant.now();
        String correlationId = CorrelationIdGenerator.newId();
        SearchEvent event = new SearchEvent(
                account.getId(),
                account.getAccountNumber(),
                account.getAccountName(),
                account.getShortName(),
                account.getOfficeCode(),
                account.getCustomerId(),
                clickedAt,
                properties.defaultUsername(),
                correlationId
        );

        accountSearchEventProducer.publish(event);

        log.info("Click registered accountId='{}', username='{}', correlationId='{}'",
                accountId, properties.defaultUsername(), correlationId);

        return new AccountClickResponse(
                accountId,
                "ACCEPTED",
                clickedAt,
                correlationId,
                                "Click captured and published to Kafka."
        );
    }
}
