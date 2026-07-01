package com.example.acctsearch.util;

import java.util.UUID;

public final class CorrelationIdGenerator {

    private CorrelationIdGenerator() {
    }

    public static String newId() {
        return UUID.randomUUID().toString();
    }
}
