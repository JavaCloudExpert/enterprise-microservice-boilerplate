# Enterprise Service — Microservice Boilerplate

> Production-grade Spring Boot microservice demonstrating Clean Architecture, CQRS, Resilience4j Circuit Breaker, and enterprise persistence patterns.

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

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 / 21 |
| Framework | Spring Boot 4.x |
| Architecture | Clean Architecture + CQRS |
| Resilience | Resilience4j Circuit Breaker |
| Persistence | Spring Data JPA · MS SQL Server · H2 (dev) |
| API Docs | OpenAPI 3.0 / Swagger UI |
| Build | Maven · GitHub Actions CI/CD |
| Quality | SonarCloud · JUnit 5 · AssertJ |

---

## Architecture

```
src/main/java/com/javacloudexpert/enterpriseservice/
├── domain/          # Entities, value objects, business rules (no framework deps)
├── application/     # Use cases, CQRS commands, Circuit Breaker orchestration
├── infrastructure/  # JPA repositories, DB adapters, external integrations
└── web/             # REST controllers, OpenAPI annotations, request/response DTOs
```

---

## Running Locally

```bash
# Clone
git clone https://github.com/JavaCloudExpert/enterprise-microservice-boilerplate.git
cd enterprise-microservice-boilerplate

# Build & test
./mvnw clean verify

# Run (H2 in-memory, MS SQL-compatible mode)
./mvnw spring-boot:run
```

API docs available at: `http://localhost:8080/swagger-ui/index.html`

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
