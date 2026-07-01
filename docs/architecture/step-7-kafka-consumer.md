# Step 7 - Kafka Consumer (recentsearch-service)

## Objective

Consume account click events from Kafka and persist recent searches into recentsearch-index.

## What Changed

- Added SearchEvent contract in recentsearch-service.
- Added Kafka consumer configuration with:
  - ConsumerFactory
  - JsonDeserializer (via ErrorHandlingDeserializer)
  - ConcurrentKafkaListenerContainerFactory
  - CommonErrorHandler (DefaultErrorHandler)
  - DeadLetterPublishingRecoverer
- Added RecentSearchEventConsumer with @KafkaListener for account-search-events.
- Added service ingestion method to map SearchEvent to RecentSearchDocument and save it.

## Reliability and Error Handling

- Retry policy: FixedBackOff(1000ms, 2 retries) => 3 delivery attempts total.
- On final failure, event is published to account-search-events-dlt.
- DLT publishing uses KafkaTemplate with JsonSerializer.

## Why This Approach

- Consumer concerns are isolated in consumer/config packages.
- Business mapping and persistence remain in service layer.
- Error handling is centralized in listener container configuration.

## Enterprise Considerations

- Explicit group ID and topic properties are externalized.
- ErrorHandlingDeserializer protects listener from deserialization crashes.
- DLT support preserves failed records for replay/inspection.
- Retrieval endpoint remains sorted by clickedAt desc for UX correctness.

## Learning Simplifications

- Single consumer group and fixed retry policy.
- No custom metrics/interceptors yet.
- No schema registry integration yet.
