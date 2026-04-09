# Health Tracker

A full-stack web application for tracking workouts, sleep, nutrition, and wellbeing.  
Built as a degree project (Examensarbete) — Java24, Jakob Gullberg.

## Tech Stack

| Layer    | Technology                                                   |
| -------- | ------------------------------------------------------------ |
| Backend  | Java 17, Spring Boot 3.4.4, Spring Security, Spring Data JPA |
| Frontend | React 19, TypeScript, Vite 8, React Router DOM               |
| Database | PostgreSQL 16 (Docker)                                       |
| Auth     | JWT (jjwt 0.12.6), BCrypt                                    |
| Styling  | Custom CSS (no UI libraries)                                 |
| Testing  | JUnit 5, Mockito, AssertJ                                    |

## Project Structure

```
health-tracker/
├── backend/                → Spring Boot REST API
│   └── src/main/java/com/gullberg/healthtracker/
│       ├── controller/     → REST controllers
│       ├── service/        → Business logic
│       ├── repository/     → Spring Data JPA repositories
│       ├── model/          → JPA entities + enums
│       ├── dto/            → Request/Response DTOs
│       ├── security/       → JWT filter, token provider, security config
│       └── exception/      → Global exception handler
├── frontend/               → React SPA
│   └── src/
│       ├── components/     → Reusable UI (AppLayout, Sidebar, Modal, NotificationToast)
│       ├── pages/          → Page components (Dashboard, Workouts, Sleep, Nutrition, Wellbeing)
│       ├── context/        → AuthContext (login/register/logout)
│       ├── hooks/          → Custom hooks (useNotificationToast)
│       ├── services/       → API wrapper with auto JWT injection
│       └── types.ts        → Shared TypeScript interfaces
└── README.md
```

## Prerequisites

- **Java 17** (e.g. Eclipse Temurin)
- **Docker Desktop** (for PostgreSQL)
- **Node.js 20** (for frontend)
- **Maven** (or use the included `mvnw` wrapper)

## Getting Started

### 1. Start the Database

```bash
# First time — create the container:
docker run -d --name healthtracker-db \
  -e POSTGRES_DB=healthtracker \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  postgres:16

# After that — just start it:
docker start healthtracker-db
```

### 2. Start the Backend

```bash
cd backend
export JAVA_HOME=$(/usr/libexec/java_home -v 17)   # macOS
./mvnw spring-boot:run
```

The API runs at **http://localhost:8080**.

### 3. Start the Frontend

```bash
cd frontend
npm install    # first time only
npm run dev
```

The app runs at **http://localhost:5173**.

### 4. Run Tests

```bash
cd backend
JAVA_HOME=$(/usr/libexec/java_home -v 17) ./mvnw test
```

## API Endpoints

All endpoints (except auth) require a valid JWT token in the `Authorization: Bearer <token>` header.

### Authentication

| Method | Endpoint             | Description         |
| ------ | -------------------- | ------------------- |
| POST   | `/api/auth/register` | Register a new user |
| POST   | `/api/auth/login`    | Login, returns JWT  |

### Workouts

| Method | Endpoint             | Description       |
| ------ | -------------------- | ----------------- |
| GET    | `/api/workouts`      | Get all workouts  |
| GET    | `/api/workouts/{id}` | Get workout by id |
| POST   | `/api/workouts`      | Create a workout  |
| PUT    | `/api/workouts/{id}` | Update a workout  |
| DELETE | `/api/workouts/{id}` | Delete a workout  |

### Sleep Logs

| Method | Endpoint               | Description         |
| ------ | ---------------------- | ------------------- |
| GET    | `/api/sleep-logs`      | Get all sleep logs  |
| GET    | `/api/sleep-logs/{id}` | Get sleep log by id |
| POST   | `/api/sleep-logs`      | Create a sleep log  |
| PUT    | `/api/sleep-logs/{id}` | Update a sleep log  |
| DELETE | `/api/sleep-logs/{id}` | Delete a sleep log  |

### Nutrition Logs

| Method | Endpoint                   | Description             |
| ------ | -------------------------- | ----------------------- |
| GET    | `/api/nutrition-logs`      | Get all nutrition logs  |
| GET    | `/api/nutrition-logs/{id}` | Get nutrition log by id |
| POST   | `/api/nutrition-logs`      | Create a nutrition log  |
| PUT    | `/api/nutrition-logs/{id}` | Update a nutrition log  |
| DELETE | `/api/nutrition-logs/{id}` | Delete a nutrition log  |

### Wellbeing Logs

| Method | Endpoint                   | Description             |
| ------ | -------------------------- | ----------------------- |
| GET    | `/api/wellbeing-logs`      | Get all wellbeing logs  |
| GET    | `/api/wellbeing-logs/{id}` | Get wellbeing log by id |
| POST   | `/api/wellbeing-logs`      | Create a wellbeing log  |
| PUT    | `/api/wellbeing-logs/{id}` | Update a wellbeing log  |
| DELETE | `/api/wellbeing-logs/{id}` | Delete a wellbeing log  |

### User

| Method | Endpoint        | Description      |
| ------ | --------------- | ---------------- |
| GET    | `/api/users/me` | Get current user |

## Features

- **JWT authentication** — Secure login/register with token-based auth
- **Dashboard** — Day-by-day overview with date navigation, summary cards for all categories, calorie balance
- **Full CRUD** — Create, read, update, and delete for workouts, sleep, nutrition, and wellbeing
- **Notification toasts** — Success/error feedback on all actions
- **Delete confirmation** — Confirm dialog before destructive actions
- **Responsive layout** — Sidebar navigation with mobile support (hamburger menu + overlay)
- **Reusable modal** — Shared form modal with ESC-close and click-outside dismiss
- **Future date prevention** — Cannot log entries for future dates

## Author

Jakob Gullberg — Java24, Examensarbete
