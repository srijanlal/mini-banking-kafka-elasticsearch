package com.example.recentsearch.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "recentsearch.kafka")
public record RecentSearchKafkaProperties(
        @NotBlank String accountSearchEventsTopic,
        @NotBlank String accountSearchEventsDltTopic,
        @NotBlank String groupId,
        boolean consumerEnabled
) {
}
