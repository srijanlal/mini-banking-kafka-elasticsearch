# Why We Use Docker for Kafka, Elasticsearch, and Kibana

## Short Answer
We use Docker so everyone can run the same infrastructure locally with one command, without manually installing and configuring Kafka, Elasticsearch, or Kibana on their machine.

## What Docker Actually Does
Docker packages an application and everything it needs (runtime, libraries, config) into an image.
When you run an image, Docker starts it as a container.

- Image: a read-only blueprint (like a template)
- Container: a running instance of that image

So instead of installing Kafka, Elasticsearch, and Kibana directly on your laptop, Docker runs each one in its own isolated container.

## How Docker Works (Simple Flow)
1. Docker downloads images (for example, Kafka or Elasticsearch images) from a registry.
2. Docker creates containers from those images.
3. Each container runs its own process (for example, Kafka broker process).
4. Docker maps container ports to your machine ports.
5. Your local apps call localhost:<port>, and Docker forwards traffic into the right container.

## How We Use Apps Running Inside Docker
The services in this repo connect to infrastructure through localhost ports exposed by Docker Compose.

- Kafka container exposes localhost:9094
- Elasticsearch container exposes localhost:9200
- Kibana container exposes localhost:5601

That means your Spring Boot services can use normal config like:

- spring.kafka.bootstrap-servers=localhost:9094
- spring.elasticsearch.uris=http://localhost:9200

And you can open Kibana in a browser at http://localhost:5601.

## Why This Access Pattern Works
Even though Kafka/Elasticsearch/Kibana run "inside" containers, port mapping makes them reachable from your host machine and from local apps running outside containers.

In short:

- Containers isolate runtime and dependencies.
- Port mapping exposes selected container services to localhost.
- Docker Compose wires everything together in one repeatable command.

## Why This Matters in This Project
This project has two Spring Boot services and one Angular frontend. The services depend on shared infrastructure:

- Kafka for asynchronous event flow between services
- Elasticsearch for search and persistence use cases
- Kibana for observability and quick data inspection

Running these components through Docker Compose gives us a predictable local platform for development and testing.

## Key Benefits

### 1. Consistent Environment Across Machines
Kafka and Elasticsearch are sensitive to version/config mismatches. Docker locks image versions and startup config, so the team gets the same behavior on each machine.

### 2. Fast Project Onboarding
A new developer can start infra with:

```bash
docker compose up -d
```

Or include Kibana with the observability profile:

```bash
docker compose --profile observability up -d
```

This removes long manual setup steps.

### 3. Isolation from Host OS
Infra dependencies run in containers, not as global OS services. This avoids conflicts with existing local installations and keeps the host clean.

### 4. Repeatable Reset and Recovery
If local data or state is corrupted, containers can be recreated quickly. The setup is declarative in docker-compose.yml, so recovery is simple and repeatable.

### 5. Production-Like Service Interaction
Even though this is local development, using real Kafka and Elasticsearch in containers better reflects real distributed behavior than in-memory substitutes.

### 6. Optional Observability via Kibana
Kibana is available only when needed using a Compose profile. This keeps default local startup lightweight while still enabling investigation workflows.

## Trade-Offs (Accepted for This Repo)

- Containers consume RAM/CPU, especially Elasticsearch.
- Startup time is slightly slower than purely in-memory mocks.

These trade-offs are acceptable because the learning goal includes real infrastructure behavior and service integration.

## Conclusion
Docker is used here to make infrastructure setup predictable, sharable, and low-friction while preserving realistic behavior for Kafka and Elasticsearch workflows, with Kibana available for visibility when needed.
