package com.example.acctsearch.mapper;

import com.example.acctsearch.document.AccountDocument;
import com.example.acctsearch.dto.AccountResponse;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountResponse toResponse(AccountDocument document) {
        return new AccountResponse(
                document.getId(),
                document.getAccountNumber(),
                document.getShortName(),
                document.getAccountName(),
                document.getCustomerId(),
                document.getOfficeCode(),
                document.getStatus()
        );
    }
}
