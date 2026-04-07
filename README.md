# Health Tracker

A web-based wellness application for tracking exercise, sleep, nutrition, and general well-being.  
Built as a degree project (Examensarbete) — Java24, Jakob Gullberg.

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
| Database | PostgreSQL 16 (Docker)                     |
| Auth     | JWT (JSON Web Tokens)                      |
| Testing  | JUnit 5, Mockito                           |
| Tools    | Maven, Git, Docker, Postman                |

## Getting Started

### Prerequisites

- Java 17 (Temurin)
- Docker Desktop
- Node.js (for frontend)
- Maven (or use the included Maven Wrapper)

### Database (PostgreSQL via Docker)

Start a PostgreSQL container:

```bash
docker run -d --name healthtracker-db \
  -e POSTGRES_DB=healthtracker \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  postgres:16
```

Useful commands:

```bash
docker start healthtracker-db   # Start the container
docker stop healthtracker-db    # Stop the container
docker ps                       # Check running containers
```

### Backend

Make sure Docker is running and the database container is started, then:

```bash
cd backend
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`

### Frontend

```bash
cd frontend
npm install
npm start
```

## API Endpoints

| Method   | Endpoint              | Description            |
| -------- | --------------------- | ---------------------- |
| `GET`    | `/api/workouts`       | Get all workouts       |
| `POST`   | `/api/workouts`       | Create a workout       |
| `GET`    | `/api/workouts/{id}`  | Get workout by id      |
| `PUT`    | `/api/workouts/{id}`  | Update a workout       |
| `DELETE` | `/api/workouts/{id}`  | Delete a workout       |
| `GET`    | `/api/sleep-logs`     | Get all sleep logs     |
| `POST`   | `/api/sleep-logs`     | Create a sleep log     |
| `GET`    | `/api/nutrition-logs` | Get all nutrition logs |
| `POST`   | `/api/nutrition-logs` | Create a nutrition log |
| `GET`    | `/api/wellbeing-logs` | Get all wellbeing logs |
| `POST`   | `/api/wellbeing-logs` | Create a wellbeing log |
| `GET`    | `/api/users/{id}`     | Get user by id         |

> All list endpoints require `?userId=` query parameter. This will be replaced by JWT authentication.

## Author

Jakob Gullberg – Java24
