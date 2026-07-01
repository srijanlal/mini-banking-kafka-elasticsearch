package com.example.acctsearch.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "acctsearch.kafka")
public record KafkaTopicsProperties(
        @NotBlank String accountSearchEventsTopic,
        @NotBlank String accountSearchEventsDltTopic
) {
}
