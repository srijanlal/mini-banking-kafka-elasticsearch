# Mini Banking Search (Learning Microservices Project)

This repository is intentionally built incrementally for learning enterprise-style architecture with manageable complexity.

## Stepwise Roadmap

1. Project architecture
2. Docker setup
3. acctsearch-service
4. Elasticsearch integration
5. Kafka producer
6. recentsearch-service
7. Kafka consumer
8. Angular UI
9. Integration testing
10. Run everything locally

## Current Status

- Completed: Step 1 (architecture skeleton)
- Completed: Step 2 (Docker infrastructure setup)
- Completed: Step 3 (acctsearch-service base implementation)
- Completed: Step 4 (Elasticsearch integration for acctsearch-service)
- Completed: Step 5 (Kafka producer in acctsearch-service)
- Completed: Step 6 (recentsearch-service base implementation)
- Completed: Step 7 (Kafka consumer in recentsearch-service)
- Completed: Step 8 (Angular UI)
- Deferred: Step 9 (integration testing) by user choice
- Completed: Step 10 (run everything locally)

## High-Level Architecture

- Angular UI calls acctsearch-service for account search and click tracking.
- acctsearch-service queries its Elasticsearch index and publishes click events to Kafka.
- recentsearch-service consumes Kafka events and stores recent searches in its own Elasticsearch index.
- recentsearch-service exposes API to return recent searches ordered by latest first.

No direct synchronous call from acctsearch-service to recentsearch-service is allowed.

## Module Layout

- acctsearch-service: Spring Boot service for account search and event publishing.
- recentsearch-service: Spring Boot service for event consumption and recent-search retrieval.
- frontend: Angular UI (to be created in Step 8).
- docs/architecture: architecture notes and decisions.

## Planned Feature-Oriented Package Structure (Per Service)

Each service will use a feature-oriented package rooted at:

- com.example.acctsearch
- com.example.recentsearch

Within the feature module, we will use focused packages only when needed:

- controller
- service
- repository
- document
- dto
- mapper
- config
- event
- producer or consumer
- exception
- util

## Why This Structure

- Keeps business flow easy to follow for learning.
- Preserves separation of responsibilities needed in production code.
- Avoids over-layering and unnecessary abstractions.

See docs/architecture/step-1-architecture.md for full details.

## Local Infrastructure (Step 2)

Docker Compose file:

- docker-compose.yml

Start core infra:

```bash
docker compose up -d
```

Start with Kibana:

```bash
docker compose --profile observability up -d
```

Key ports:

- Kafka external: 9094
- Elasticsearch: 9200
- Kibana: 5601

Details: docs/architecture/step-2-docker-setup.md

## acctsearch-service (Step 3)

Implemented APIs:

- GET /api/accounts/search?query=
- POST /api/accounts/{id}/click

Run service:

1. cd acctsearch-service
2. mvn spring-boot:run

Service URL:

- http://localhost:8100

Details: docs/architecture/step-3-acctsearch-service.md

## Elasticsearch Integration (Step 4)

acctsearch-service now uses Elasticsearch index acctsearch-index for account search and account lookup.

Startup behavior:

- If acctsearch-index is empty, the app inserts 120 sample accounts.

Details: docs/architecture/step-4-elasticsearch-integration.md

## Kafka Producer (Step 5)

acctsearch-service now publishes SearchEvent JSON messages when an account is clicked.

Kafka topic:

- account-search-events

DLT topic:

- account-search-events-dlt

Details: docs/architecture/step-5-kafka-producer.md

## recentsearch-service (Step 6)

Implemented API:

- GET /api/recent-searches

Run service:

1. cd recentsearch-service
2. mvn spring-boot:run

Service URL:

- http://localhost:8200

Details: docs/architecture/step-6-recentsearch-service.md

## Kafka Consumer (Step 7)

recentsearch-service now consumes SearchEvent messages from Kafka topic account-search-events and stores them in recentsearch-index.

Retry + DLT behavior:

- Retry with fixed backoff
- On final failure, publish to account-search-events-dlt

Details: docs/architecture/step-7-kafka-consumer.md

## Angular UI (Step 8)

Frontend app path:

- frontend/bank-ui

Run frontend:

1. cd frontend/bank-ui
2. npm start

Default UI URL:

- http://localhost:4200

Details: docs/architecture/step-8-angular-ui.md

## Run Everything Locally (Step 10)

Run guide:

- docs/architecture/step-10-run-everything-locally.md

Helper scripts:

- scripts/run-local.ps1
- scripts/stop-local.ps1

Run all apps quickly from repo root:

- powershell -ExecutionPolicy Bypass -File .\scripts\run-local.ps1
- powershell -ExecutionPolicy Bypass -File .\scripts\run-local.ps1 -WithKibana
