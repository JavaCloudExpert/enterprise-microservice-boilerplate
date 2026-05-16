# Enterprise Service — Spring Boot Microservice Boilerplate

> Production-grade Spring Boot 4.x microservice demonstrating **Clean Architecture**, **CQRS**, **Resilience4j Circuit Breaker**, proactive **CVE dependency management**, and enterprise-grade persistence patterns targeting **MS SQL Server / DB2** environments.

## CI/CD & Code Quality

[![CI/CD Pipeline](https://github.com/JavaCloudExpert/enterprise-microservice-boilerplate/actions/workflows/pipeline.yaml/badge.svg)](https://github.com/JavaCloudExpert/enterprise-microservice-boilerplate/actions/workflows/pipeline.yaml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=JavaCloudExpert_enterprise-microservice-boilerplate2&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=JavaCloudExpert_enterprise-microservice-boilerplate2)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=JavaCloudExpert_enterprise-microservice-boilerplate2&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=JavaCloudExpert_enterprise-microservice-boilerplate2)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=JavaCloudExpert_enterprise-microservice-boilerplate2&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=JavaCloudExpert_enterprise-microservice-boilerplate2)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=JavaCloudExpert_enterprise-microservice-boilerplate2&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=JavaCloudExpert_enterprise-microservice-boilerplate2)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=JavaCloudExpert_enterprise-microservice-boilerplate2&metric=coverage)](https://sonarcloud.io/summary/new_code?id=JavaCloudExpert_enterprise-microservice-boilerplate2)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=JavaCloudExpert_enterprise-microservice-boilerplate2&metric=bugs)](https://sonarcloud.io/summary/new_code?id=JavaCloudExpert_enterprise-microservice-boilerplate2)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=JavaCloudExpert_enterprise-microservice-boilerplate2&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=JavaCloudExpert_enterprise-microservice-boilerplate2)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=JavaCloudExpert_enterprise-microservice-boilerplate2&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=JavaCloudExpert_enterprise-microservice-boilerplate2)

---

## What This Demonstrates

This is a focused, production-ready reference implementation built the way real enterprise systems need to be built — not just "it works", but maintainable, observable, secure, and testable.

| Concern | Approach |
|---|---|
| **Architecture** | 4-layer Clean Architecture — `domain/` has zero framework imports |
| **Security** | Proactive CVE pinning (Tomcat, Jackson, BouncyCastle) + OWASP Dependency-Check in CI |
| **Resilience** | Resilience4j Circuit Breaker with graceful degradation fallback |
| **Error Handling** | RFC 9457 `ProblemDetail` — the Spring 6 / HTTP standard for machine-readable errors |
| **Persistence** | MS SQL/DB2-compatible JPA patterns: UUID-as-VARCHAR, `BigDecimal(19,4)`, optimistic locking |
| **Code Quality** | Spotless + Google Java Format enforced at build time — unformatted code fails CI |
| **Testing** | Layered tests: unit (domain), slice (`@WebMvcTest`), integration (`@DataJpaTest`) |
| **API Docs** | OpenAPI 3.0 / Swagger UI auto-generated from code annotations |

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.x |
| Architecture | Clean Architecture + CQRS |
| Resilience | Resilience4j Circuit Breaker |
| Persistence | Spring Data JPA · MS SQL Server · H2 in `MODE=MSSQLServer` (local) |
| API Docs | OpenAPI 3.0 / Swagger UI (springdoc) |
| Build | Maven · Spotless · JaCoCo |
| CI/CD | GitHub Actions |
| Quality | SonarCloud · JUnit 5 · AssertJ |
| Security | OWASP Dependency-Check · CVE pin overrides in BOM |

---

## Architecture

```
src/main/java/com/javacloudexpert/enterpriseservice/
├── domain/          # Transaction entity, TransactionStatus, TransactionNotFoundException
│                    # — ZERO framework imports; pure business logic
├── application/     # TransactionApplicationService — orchestrates use cases
│                    # ProcessTransactionCommand record (CQRS) with Bean Validation
├── infrastructure/  # TransactionJpaEntity, TransactionRepository, TransactionMapper
│                    # — only layer allowed to touch JPA
└── web/             # TransactionController, TransactionResponse DTO
                     # GlobalExceptionHandler → RFC 9457 ProblemDetail
```

### Data Flow: `POST /api/v1/transactions`

```
Controller  →  ProcessTransactionCommand (validated record)
           →  ApplicationService.execute()
           →  new Transaction(amount, currency)   ← business rules enforced here
           →  transaction.complete()              ← state machine: PENDING → COMPLETED
           →  TransactionMapper.toJpaEntity()
           →  TransactionRepository.save()
```

### Domain State Machine

```
[PENDING] ──complete()──► [COMPLETED]
[PENDING] ──fail()──────► [FAILED]
Calling complete()/fail() on a non-PENDING transaction throws IllegalStateException → HTTP 422
```

---

## Security — CVE Fixes Applied

All dependency vulnerabilities are pinned directly in `pom.xml` with inline comments explaining each fix. Not suppressed — **actually resolved**.

| Dependency | Pinned Version | CVEs Fixed |
|---|---|---|
| Apache Tomcat | `11.0.21` (via Spring Boot BOM property) | CVE-2026-34487 (HIGH), CVE-2026-34483 (HIGH), CVE-2026-34500 (MEDIUM) |
| Jackson | `3.1.2` (jackson-bom imported before spring-cloud-dependencies) | GHSA-2m67-wjpj-xhg9 (HIGH) in jackson-core |
| BouncyCastle | `1.84` (explicit override — not managed by Spring BOM) | CVE-2026-0636 (MEDIUM), CVE-2026-5598 (HIGH) |

OWASP Dependency-Check runs as a dedicated CI step and produces HTML/JSON/XML reports under `target/`. False positives are tracked in `dependency-check-suppressions.xml`.

---

## Enterprise Persistence Patterns

These conventions are non-obvious but critical in finance/insurance systems running on MS SQL Server or DB2:

```java
@Entity
@Table(name = "transactions")               // explicit — avoids reserved-word collisions on DB2/MSSQL
public class TransactionJpaEntity {

    @Id
    @JdbcTypeCode(java.sql.Types.VARCHAR)    // UUID stored as VARCHAR — portable across all DB vendors
    private UUID id;

    @Column(precision = 19, scale = 4)       // ISO 4217 standard for monetary amounts
    private BigDecimal amount;

    @Version
    private Long version;                    // optimistic locking — prevents lost updates under concurrency
}
```

Local dev and all tests use H2 in `MODE=MSSQLServer` — no SQL Server installation required, but query behaviour mirrors production.

---

## Resilience: Circuit Breaker

```java
@CircuitBreaker(name = "transactionService", fallbackMethod = "processFallback")
public void execute(ProcessTransactionCommand command) { ... }

@SuppressWarnings("unused")  // invoked reflectively by Resilience4j
private void processFallback(ProcessTransactionCommand command, Throwable t) {
    // production hook: publish to dead-letter queue / DB2 staging table
    throw new RuntimeException("Service temporarily unavailable. Please try again later.");
}
```

Configured in `application.yaml`: sliding window = 10, failure threshold = 50%, wait in open state = 10 s.

---

## Error Handling — RFC 9457 ProblemDetail

All API errors return a structured `ProblemDetail` response (HTTP standard, Spring 6+):

```json
{
  "type": "https://api.javacloudexpert.com/errors/transaction-not-found",
  "title": "Transaction Not Found",
  "status": 404,
  "detail": "Transaction not found with id: 3fa85f64-..."
}
```

| Exception | HTTP Status |
|---|---|
| `TransactionNotFoundException` | 404 Not Found |
| `MethodArgumentNotValidException` | 400 Bad Request |
| `IllegalArgumentException` | 400 Bad Request |
| `IllegalStateException` | 422 Unprocessable Entity |
| `RuntimeException` (incl. Circuit Breaker open) | 503 Service Unavailable |

---

## Code Quality Pipeline

```
./mvnw verify
  │
  ├── compile        (Lombok annotation processing)
  ├── test           (JUnit 5 — domain unit, application unit, web slice, JPA integration)
  ├── jacoco:report  (coverage report → target/site/jacoco/index.html)
  └── spotless:check ← FAILS BUILD if any file is not formatted
                        (Google Java Format, AOSP 4-space indent)
```

Auto-fix formatting before pushing:

```bash
./mvnw spotless:apply
```

---

## Running Locally

```bash
# Clone & build
git clone https://github.com/JavaCloudExpert/enterprise-microservice-boilerplate.git
cd enterprise-microservice-boilerplate
./mvnw clean verify

# Run (H2 in-memory, MS SQL Server-compatible mode)
./mvnw spring-boot:run
```

| URL | Description |
|---|---|
| `http://localhost:8080/swagger-ui/index.html` | Interactive API documentation |
| `http://localhost:8080/h2-console` | In-memory DB browser (JDBC URL: `jdbc:h2:mem:devdb`) |

```bash
# OWASP CVE scan (not part of verify — run explicitly)
./mvnw dependency-check:check -DnvdApiKey=<your-key>
```

---

## Roadmap / Planned Features

- [ ] **Actuator + Health endpoints** — `/actuator/health`, `/actuator/info` for container orchestration readiness probes
- [ ] **Flyway migrations** — versioned schema management; replaces `ddl-auto: update` for production
- [ ] **Docker + docker-compose** — one-command local environment with SQL Server container
- [ ] **Spring Security (JWT)** — stateless Bearer token authentication layer
- [ ] **Micrometer custom metrics** — circuit breaker counters and transaction throughput exposed to Prometheus
- [ ] **Pagination** — `GET /api/v1/transactions?page=0&size=20` with Spring Data `Pageable`
- [ ] **Spring Data Auditing** — `@CreatedBy` / `@LastModifiedBy` for regulatory audit trails
- [ ] **Idempotency Key header** — prevent duplicate transactions on client retry
- [ ] **Testcontainers (SQL Server)** — integration tests against a real SQL Server instance in CI

---

## About the Author

**JavaCloudExpert** — Senior Technical Consultant | Enterprise Java & Cloud Architect

With **18 years of experience** building high-availability, scalable distributed systems across finance, insurance, and logistics.

### 🚀 Core Expertise
- **Backend:** Java, Spring Boot, Spring Batch, Resilience4j, CQRS
- **Data Architecture:** MS SQL Server, DB2 (Legacy Modernization), Data Persistence Patterns
- **Cloud & DevOps:** Azure, AWS, GitHub Actions, CI/CD Pipelines, SonarQube
- **Architecture:** Clean Architecture, DDD, OpenAPI/Swagger, Async Parallel Processing

### 💼 Available for Consulting
Architectural reviews · System design · High-stakes implementation projects

📫 architect.enterprise.18@gmail.com
