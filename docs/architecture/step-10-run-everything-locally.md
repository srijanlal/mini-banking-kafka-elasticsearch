# Step 10 - Run Everything Locally

## Objective

Run the full mini banking system locally:

- Infrastructure via Docker Compose
- acctsearch-service
- recentsearch-service
- Angular frontend

## Prerequisites

- Docker Desktop running
- Java 21 available
- Maven available
- Node.js and npm available

## Recommended Startup Order

1. Start infrastructure
2. Start recentsearch-service
3. Start acctsearch-service
4. Start Angular frontend

Starting recentsearch first helps ensure the Kafka consumer is ready when click events start flowing.

## Manual Commands

From repository root:

1) Start infrastructure:

docker compose up -d

2) Start recentsearch-service:

cd recentsearch-service
mvn spring-boot:run

3) Start acctsearch-service:

cd acctsearch-service
mvn spring-boot:run

4) Start Angular frontend:

cd frontend/bank-ui
npm start

## Service URLs

- Angular UI: http://localhost:4200
- acctsearch-service: http://localhost:8100
- recentsearch-service: http://localhost:8200
- Elasticsearch: http://localhost:9200
- Kafka external listener: localhost:9094
- Kibana (optional profile): http://localhost:5601

## Quick Start Scripts

Use scripts from repository root:

- scripts/run-local.ps1
- scripts/stop-local.ps1

Recommended command:

powershell -ExecutionPolicy Bypass -File .\scripts\run-local.ps1

With Kibana:

powershell -ExecutionPolicy Bypass -File .\scripts\run-local.ps1 -WithKibana

## Smoke Flow

1. Open UI at http://localhost:4200
2. Search with at least 2 characters
3. Click one search result
4. Verify Recent Searches updates after click

## Enterprise Notes

- This startup sequence mirrors independent deployable units.
- Kafka decouples click producer from recent search persistence.
- Elasticsearch indices remain service-owned and independent.
