package com.example.acctsearch.service;

import com.example.acctsearch.dto.AccountSearchResponse;

public interface AccountSearchService {

    AccountSearchResponse search(String query);
}
