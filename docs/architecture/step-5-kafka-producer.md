# Step 5 - Kafka Producer (acctsearch-service)

## Objective

Publish account click events to Kafka without directly calling recentsearch-service.

## What Changed

- Added SearchEvent JSON contract.
- Added explicit Kafka producer configuration with:
  - ProducerFactory
  - KafkaTemplate
  - JsonSerializer
- Added AccountSearchEventProducer component.
- Updated click flow to publish SearchEvent to account-search-events topic.
- Added retry and idempotence producer settings.
- Added best-effort DLT fallback publish to account-search-events-dlt on primary send failure.

## Event Contract

SearchEvent fields:

- accountId
- accountNumber
- accountName
- shortName
- officeCode
- customerId
- clickedAt
- username
- correlationId

## Why This Approach

- Producer is separated from service logic to keep click use case readable.
- Typed event record improves schema clarity and consistency.
- Explicit producer config demonstrates enterprise primitives clearly for learning.

## Enterprise Considerations

- acks=all and idempotence improve delivery reliability.
- Retries with backoff handle transient broker/network failures.
- DLT topic fallback preserves failed events for later analysis/replay.
- Correlation ID is included for traceability across services.

## Learning Simplifications

- DLT fallback is implemented as concise best-effort producer logic.
- Full consumer-side ErrorHandler and retry/DLT processing will be implemented in Step 7.
