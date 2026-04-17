# Health Tracker — Backend

Spring Boot REST API for the Health Tracker application.

For full setup instructions, prerequisites, and how to run the complete application, see the [main README](../README.md) in the project root.

## Quick Reference

- **Language:** Java 17
- **Framework:** Spring Boot 3.4.4, Spring Security, Spring Data JPA
- **Database:** PostgreSQL 16 (Docker)
- **Auth:** JWT (jjwt 0.12.6), BCrypt
- **Testing:** JUnit 5, Mockito, AssertJ
- **IDE:** IntelliJ IDEA

## Run

Open `backend/` in IntelliJ IDEA → run `HealthTrackerSpringApplication`.

See [main README — Step 2](../README.md#step-2--start-the-backend) for detailed instructions.

## Test

Right-click `src/test` in IntelliJ → **Run All Tests**, or:

```bash
./mvnw test
```

## Configuration

All settings are in `src/main/resources/application.properties`.  
See the [main README — Configuration](../README.md#configuration) for details.

## Package Structure

```
src/main/java/com/gullberg/healthtracker/
├── config/         → Security & CORS configuration
├── controller/     → REST controllers
├── dto/            → Request / response DTOs
├── exception/      → Global exception handler
├── model/          → JPA entities & enums
├── repository/     → Spring Data JPA repositories
├── security/       → JWT filter, token provider, UserDetailsService
└── service/        → Business logic & validation
```

## Implementation Notes

- Hibernate uses `ddl-auto=update` for local schema management.
- Lombok is used in the backend codebase.
- The project includes the Maven Wrapper, so a separate Maven installation is optional.
