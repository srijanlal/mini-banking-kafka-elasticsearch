package com.example.acctsearch.controller;

import com.example.acctsearch.dto.AccountClickResponse;
import com.example.acctsearch.dto.AccountSearchResponse;
import com.example.acctsearch.service.AccountClickService;
import com.example.acctsearch.service.AccountSearchService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AccountSearchController {

    private final AccountSearchService accountSearchService;
    private final AccountClickService accountClickService;

    @GetMapping("/search")
    public AccountSearchResponse searchAccounts(@RequestParam("query") @NotBlank @Size(min = 2, max = 50) String query) {
        return accountSearchService.search(query);
    }

    @PostMapping("/{id}/click")
    public AccountClickResponse clickAccount(@PathVariable("id") @NotBlank String id) {
        return accountClickService.registerClick(id);
    }
}
