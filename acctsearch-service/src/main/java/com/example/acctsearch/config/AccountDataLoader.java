package com.example.acctsearch.config;

import com.example.acctsearch.document.AccountDocument;
import com.example.acctsearch.repository.AccountDocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "acctsearch.data-loader", name = "enabled", havingValue = "true", matchIfMissing = true)
public class AccountDataLoader implements ApplicationRunner {

    private final AccountDocumentRepository accountDocumentRepository;

    @Override
    public void run(ApplicationArguments args) {
        long existing = accountDocumentRepository.count();
        if (existing > 0) {
            log.info("acctsearch-index already contains {} documents. Skipping sample data loading.", existing);
            return;
        }

        List<AccountDocument> sampleAccounts = IntStream.rangeClosed(1, 120)
                .mapToObj(this::buildAccount)
                .toList();

        accountDocumentRepository.saveAll(sampleAccounts);
        log.info("Loaded {} sample accounts into acctsearch-index.", sampleAccounts.size());
    }

    private AccountDocument buildAccount(int i) {
        String numericId = String.format("%04d", i);
        return AccountDocument.builder()
                .id("a-" + numericId)
                .accountNumber("100" + String.format("%05d", i))
                .shortName("ACC" + numericId)
                .accountName("Account Holder " + numericId)
                .customerId("CUST-" + numericId)
                .officeCode("OFF-" + String.format("%03d", (i % 10) + 1))
                .status(i % 12 == 0 ? "SUSPENDED" : "ACTIVE")
                .build();
    }
}
