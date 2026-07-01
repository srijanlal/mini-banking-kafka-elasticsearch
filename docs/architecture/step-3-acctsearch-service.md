# Step 3 - acctsearch-service Base Implementation

## Objective

Create a clean and runnable Spring Boot service with required REST endpoints:

- GET /api/accounts/search?query=
- POST /api/accounts/{id}/click

## What Is Implemented in This Step

- Spring Boot app bootstrap
- Feature-oriented package skeleton
- API DTOs using Java records
- Service interfaces and concrete implementations
- Global exception handling
- Validation and meaningful logging
- Externalized properties and local profile

## Intentional Learning Simplification

Search and click lookup currently use an in-memory catalog so API behavior can be tested before integrating Elasticsearch and Kafka in later steps.

## Next Step Mapping

- Step 4: Replace in-memory account catalog with Elasticsearch repository and index-backed querying.
- Step 5: Replace click placeholder response path with Kafka producer event publishing.
