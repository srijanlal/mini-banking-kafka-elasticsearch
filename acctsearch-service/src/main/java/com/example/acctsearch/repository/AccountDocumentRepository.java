package com.example.acctsearch.repository;

import com.example.acctsearch.document.AccountDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface AccountDocumentRepository extends ElasticsearchRepository<AccountDocument, String> {

    Page<AccountDocument> findByAccountNumberContainingIgnoreCaseOrShortNameContainingIgnoreCaseOrAccountNameContainingIgnoreCaseOrCustomerIdContainingIgnoreCaseOrOfficeCodeContainingIgnoreCase(
            String accountNumber,
            String shortName,
            String accountName,
            String customerId,
            String officeCode,
            Pageable pageable
    );
}
