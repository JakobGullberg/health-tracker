# Health Tracker

A web-based wellness application for tracking exercise, sleep, nutrition, and general well-being.

## Project Structure

```
health-tracker/
├── backend/     → Java 17, Spring Boot, REST API, JPA/Hibernate, PostgreSQL
├── frontend/    → JavaScript, React (HTML/CSS)
└── README.md
```

## Tech Stack

| Layer    | Technology                                 |
| -------- | ------------------------------------------ |
| Backend  | Java 17, Spring Boot, Spring Security, JPA |
| Frontend | JavaScript, React, HTML/CSS                |
| Database | PostgreSQL                                 |
| Auth     | JWT (JSON Web Tokens)                      |
| Testing  | JUnit 5, Mockito                           |
| Tools    | Maven, Git, Postman                        |

## Getting Started

### Prerequisites

- Java 17
- PostgreSQL
- Node.js (for frontend)
- Maven (or use the included Maven Wrapper)

### Backend

```bash
cd backend
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`

### Frontend

```bash
cd frontend
npm install
npm start
```

## Author

Jakob Gullberg – Java24
