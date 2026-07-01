# Step 2 - Docker Setup

## Objective

Run local infrastructure for event-driven microservices:

- Kafka (KRaft mode)
- Elasticsearch
- Kibana (optional)

## Why KRaft Instead of ZooKeeper

Kafka KRaft mode simplifies local setup by removing ZooKeeper while keeping modern Kafka behavior. This reduces moving parts for learning.

## Services and Ports

- Kafka internal broker listener: 9092 (used by containers)
- Kafka external listener: 9094 (used by local Spring apps)
- Elasticsearch HTTP: 9200
- Kibana UI: 5601 (only when observability profile is enabled)

## Topic Initialization

A one-off init container creates:

- account-search-events
- account-search-events-dlt

This keeps startup deterministic and avoids relying on topic auto-creation.

## Run Commands

Start core infra:

```bash
docker compose up -d
```

Start core infra plus Kibana:

```bash
docker compose --profile observability up -d
```

Check containers:

```bash
docker compose ps
```

Stop infra:

```bash
docker compose down
```

Stop and remove data volumes:

```bash
docker compose down -v
```

## Enterprise Notes

- Topic DLT is pre-created so retry/DLT patterns in Spring Kafka can be demonstrated cleanly.
- Health checks improve startup reliability for dependent services.
- Separate data volumes preserve Kafka and Elasticsearch state across restarts.

## Learning Simplifications

- Plaintext Kafka listeners are enabled for local development.
- Elasticsearch security is disabled locally to avoid certificate setup complexity.
