package com.example.recentsearch.config;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "recentsearch")
public record RecentSearchProperties(
        @Min(1) @Max(100) int maxResults
) {
}
