# Step 4 - Elasticsearch Integration (acctsearch-service)

## Objective

Use Elasticsearch as the account search source of truth.

## What Changed

- Added Spring Data Elasticsearch dependency.
- Added AccountDocument mapped to index acctsearch-index.
- Added AccountDocumentRepository.
- Replaced in-memory search with repository-backed search.
- Replaced in-memory click account lookup with repository existence check.
- Added startup data loader to insert about 100 sample accounts when index is empty.

## Why This Approach

- Repository abstraction keeps code concise and testable.
- A dedicated document model keeps API DTOs separate from persistence schema.
- Startup seed data enables predictable local learning without external SQL databases.

## Enterprise Considerations

- Data loading is idempotent (runs only when index is empty).
- Externalized Elasticsearch URL and loader toggle via configuration.
- Feature package separation remains intact.

## Learning Simplifications

- Single Elasticsearch node and simple query strategy.
- No custom analyzer/mapping tuning yet.
- No pagination API yet, but search size is configuration-driven.
