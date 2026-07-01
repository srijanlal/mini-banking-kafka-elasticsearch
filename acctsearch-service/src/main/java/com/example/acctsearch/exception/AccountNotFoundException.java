package com.example.acctsearch.exception;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String accountId) {
        super("Account not found for id: " + accountId);
    }
}
