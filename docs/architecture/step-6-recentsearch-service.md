# Step 6 - recentsearch-service Base Implementation

## Objective

Create a clean and runnable recentsearch-service with API contract:

- GET /api/recent-searches

## What Is Implemented

- Spring Boot service bootstrap
- Elasticsearch document and repository for recentsearch-index
- Service layer returning recent searches ordered by clickedAt desc
- DTOs and mapper for API responses
- Global exception handler and validation support
- Externalized configuration and local profile

## Why This Approach

- The service can be run and verified independently before Kafka consumer wiring.
- Using recentsearch-index now avoids redesign when Step 7 ingestion starts.
- Layer boundaries remain simple and production-like.

## Enterprise Considerations

- Response limit is validated and property-capped.
- Read path is repository-backed and ordered at query level.
- Persistence model and API DTOs are separated.

## Learning Simplifications

- No Kafka consumer yet in this step.
- The index is expected to be populated in Step 7 from Kafka events.
