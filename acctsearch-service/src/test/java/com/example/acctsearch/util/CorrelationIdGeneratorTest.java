package com.example.acctsearch.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CorrelationIdGeneratorTest {

    @Test
    void newId_returnsNonNullUuidString() {
        // Act
        String id = CorrelationIdGenerator.newId();

        // Assert — UUID format: 8-4-4-4-12 hex chars
        assertThat(id).isNotNull().isNotBlank();
        assertThat(id).matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
    }

    @Test
    void newId_returnsUniqueIds() {
        // Act
        String id1 = CorrelationIdGenerator.newId();
        String id2 = CorrelationIdGenerator.newId();

        // Assert
        assertThat(id1).isNotEqualTo(id2);
    }
}
