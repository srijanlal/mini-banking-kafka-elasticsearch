package com.example.acctsearch.config;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "acctsearch")
public record AcctSearchProperties(
        @Min(1) @Max(100) int maxResults,
        String defaultUsername
) {
}
