package com.example.recentsearch.controller;

import com.example.recentsearch.dto.RecentSearchResponse;
import com.example.recentsearch.service.RecentSearchService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/recent-searches")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class RecentSearchController {

    private final RecentSearchService recentSearchService;

    @GetMapping
    public RecentSearchResponse getRecentSearches(@RequestParam(name = "limit", defaultValue = "20") @Min(1) @Max(100) int limit) {
        return recentSearchService.getRecentSearches(limit);
    }
}
