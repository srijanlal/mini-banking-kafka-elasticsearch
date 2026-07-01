# Step 1 - Project Architecture

## Objective

Establish a clean, scalable project shape before implementation.

## Services and Responsibilities

### acctsearch-service

- Owns account search use case.
- Owns acctsearch-index in Elasticsearch.
- Publishes account click events to Kafka topic account-search-events.

### recentsearch-service

- Owns recent search history use case.
- Consumes account click events from Kafka topic account-search-events.
- Owns recentsearch-index in Elasticsearch.
- Exposes recent searches API ordered by clickedAt descending.

## Data and Event Boundaries

- Data ownership is independent by service index.
- Communication between services is asynchronous through Kafka only.
- This prevents tight coupling and supports scalability.

## Planned API Surface

### acctsearch-service

- GET /api/accounts/search?query=
- POST /api/accounts/{id}/click

### recentsearch-service

- GET /api/recent-searches

## Planned Event Contract

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

JSON serialization will be used.

## Planned Technology Choices and Why

- Spring Boot 3.x: modern conventions and production support.
- Java 21: current LTS and clean language features.
- Spring Data Elasticsearch: repository integration with Elasticsearch.
- Spring Kafka: robust producer and consumer integration.
- Docker Compose: reproducible local environment.
- Angular: reactive UI for search and recent activity display.

## Simplifications for Learning

- No relational database.
- Hardcoded username initially.
- Concise DLT and retry setup to learn the pattern without too much ceremony.
- Single-page UI.

## Enterprise Considerations We Will Add Incrementally

- Constructor injection and immutable DTOs.
- Validation and global exception handling.
- Correlation ID propagation and meaningful logging.
- Externalized configuration and profiles.
- Integration tests with test slices where useful.
