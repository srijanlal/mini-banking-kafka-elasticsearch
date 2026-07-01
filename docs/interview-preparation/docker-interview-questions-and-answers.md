# Docker Interview Questions and Answers

## How to Use This Sheet
- Start with Core Questions to build confidence.
- Practice Tricky Questions to handle deeper follow-ups.
- Keep answers short first, then expand with examples.

## Core Questions

### 1. What is Docker?
Docker is a platform to package, ship, and run applications in containers. A container includes app code plus runtime dependencies, so it behaves consistently across environments.

### 2. What is the difference between an image and a container?
- Image: read-only blueprint.
- Container: running instance of an image.
You can create many containers from one image.

### 3. What problem does Docker solve?
It reduces environment mismatch ("works on my machine" problem), speeds up onboarding, and makes deployments more predictable.

### 4. Is Docker the same as a virtual machine?
No. VMs virtualize hardware and each VM has a full guest OS. Containers share the host OS kernel, so they are lighter and start faster.

### 5. What is Dockerfile?
A text file with instructions to build an image, for example selecting a base image, copying files, installing dependencies, and setting startup command.

### 6. What is Docker Compose?
Docker Compose defines and runs multi-container apps (services, networks, volumes) using a YAML file.

### 7. Why use Docker Compose instead of many docker run commands?
Compose is declarative, versionable, and repeatable. One command can start the full stack with dependencies and shared network.

### 8. What is the role of EXPOSE in Dockerfile?
EXPOSE documents which port the app listens on inside the container. It does not publish the port to host by itself.

### 9. What is port mapping?
It binds host port to container port, for example host 9200 to container 9200, so host apps can reach containerized services.

### 10. What are Docker volumes?
Volumes store persistent data outside container writable layer. Data survives container restart/recreation.

### 11. Why should containers be stateless when possible?
Stateless containers are easier to scale, replace, and recover. Stateful data should live in external stores or volumes.

### 12. How do containers communicate with each other in Compose?
Services on the same Compose network can reach each other by service name, for example kafka:9092.

### 13. What is the difference between CMD and ENTRYPOINT?
- ENTRYPOINT defines the main executable.
- CMD provides default arguments (or default command if ENTRYPOINT absent).
They are often used together.

### 14. What is a multi-stage build?
A Dockerfile technique with multiple stages to compile/build in one stage and copy only runtime artifacts into a smaller final image.

### 15. Why keep images small?
Smaller images build faster, pull faster, start faster, and usually reduce attack surface.

## Tricky Interview Questions

### 16. If EXPOSE is set, why is my app still not reachable from host?
Because EXPOSE does not publish ports. You must publish with run or compose ports mapping.

### 17. Why can one service connect to kafka:9092 but host app needs localhost:9094?
Inside Docker network, services use container/service endpoints. From host machine, you must use published host port.

### 18. What happens to data if I delete a container?
Container writable layer is deleted. Named volumes remain unless explicitly removed.

### 19. Why is running as root inside container risky if container is isolated?
Container isolation is not a complete security boundary. Root in container can increase impact if there is a breakout or host misconfiguration.

### 20. Why can container startup succeed but application still fail?
Container status only means process started. App may still be unready (waiting for DB, migrations, or downstream services). Use health checks/readiness checks.

### 21. Why do we pin image versions instead of using latest?
Pinned versions improve reproducibility and avoid unexpected breakage from upstream changes.

### 22. If containers are ephemeral, how do logs survive?
Use centralized logging or mount log volumes. Better practice is logging to stdout and collecting logs via platform tooling.

### 23. Why does a rebuilt image sometimes not include code changes?
Common reasons:
- Build cache reused old layers.
- Files excluded by dockerignore.
- Wrong build context path.

### 24. Difference between copy and bind mount, and when to use each?
- Copy (during image build): creates immutable artifact in image.
- Bind mount (runtime): maps host directory for live changes.
Use copy for production images, bind mounts for local development iteration.

### 25. Why is one process per container a recommended pattern, not a strict rule?
Single main concern improves observability and scaling, but sidecar-like patterns or supervised multi-process containers can be valid for specific cases.

### 26. Why do health checks sometimes make startup look slower?
Health checks reveal true readiness. Without them, app may appear up but fail first real request.

### 27. Can two containers use the same container port?
Yes, if they are different containers. Conflict happens when mapping same host port to multiple containers.

### 28. Why do I get connection refused right after compose up?
Dependency service may not be ready yet. Startup order is not equal to readiness. Add retries and health-based startup logic.

### 29. What is dockerignore and why is it important?
It excludes files from build context, reducing build time and avoiding accidental inclusion of secrets, caches, or large directories.

### 30. Why might apt-get install fail in Docker build intermittently?
Possible causes: mirror/network issues, stale package metadata, proxy limits. Typical mitigation: update package lists in same layer and keep Dockerfile deterministic.

## Scenario-Based Questions

### 31. Your image is 1.5 GB. How do you reduce it?
- Use slim/alpine base where compatible.
- Apply multi-stage build.
- Remove build tools from final stage.
- Clean package manager caches.
- Avoid copying unnecessary files.

### 32. App works locally but fails in container with file not found. Why?
Likely wrong working directory, incorrect copy path, case-sensitive path mismatch, or missing file excluded by dockerignore.

### 33. How do you pass environment-specific config safely?
Use environment variables, Compose env files, or secret managers. Avoid baking sensitive config into image layers.

### 34. In an interview, how do you explain Docker networking simply?
Each container gets network isolation and an IP on a Docker network. Containers talk internally via service names, and selected ports can be published to host.

## Rapid-Fire One-Liners
- Docker image = blueprint; container = running instance.
- EXPOSE documents; ports publishes.
- Volume persists data; writable layer is ephemeral.
- Compose startup order is not readiness.
- Pin versions for reproducible builds.
- Health checks verify availability, not just process start.

## Practice Tip
Pick any 5 tricky questions and answer each in 30 seconds and 2 minutes. Interviewers often test both concise clarity and depth.