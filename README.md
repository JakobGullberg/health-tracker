# Health Tracker

A full-stack web application for tracking workouts, sleep, nutrition, and wellbeing.  
Developed as a degree project (Examensarbete) by **Jakob Gullberg**, Java24.

---

## About the Project

Health Tracker lets users register an account, log in, and track daily health data across four categories: **workouts**, **sleep**, **nutrition**, and **wellbeing**. A dashboard provides a day-by-day overview with summary cards and calorie balance.

The project is not deployed as a live service. To review the application, clone this repository and follow the step-by-step instructions below to run it locally.

---

## Tech Stack

| Layer     | Technology                                                           |
| --------- | -------------------------------------------------------------------- |
| Backend   | Java 17, Spring Boot 3.4.4, Spring Security, Spring Data JPA, Lombok |
| Frontend  | React 19, TypeScript, Vite 8, React Router DOM                       |
| Database  | PostgreSQL 16 (runs in Docker)                                       |
| Auth      | JWT (jjwt 0.12.6), BCrypt                                            |
| Styling   | Custom CSS (no UI library)                                           |
| Testing   | JUnit 5, Mockito, AssertJ                                            |
| Dev tools | IntelliJ IDEA, Docker Desktop                                        |

---

## Features

- JWT-based authentication (register / login)
- Dashboard with date navigation and summary cards for all categories
- Full CRUD for workouts, sleep logs, nutrition logs, and wellbeing logs
- Responsive sidebar layout with mobile hamburger menu
- Reusable modal dialogs with ESC-close and click-outside dismiss
- Notification toasts for success and error feedback
- Delete confirmation before destructive actions
- Future date prevention — users cannot log entries for dates ahead of today

---

## Project Structure

```
health-tracker/
├── backend/                       → Spring Boot REST API (IntelliJ IDEA)
│   └── src/main/java/com/gullberg/healthtracker/
│       ├── config/                → Security & CORS configuration
│       ├── controller/            → REST controllers
│       ├── dto/                   → Request / response DTOs
│       ├── exception/             → Global exception handler
│       ├── model/                 → JPA entities & enums
│       ├── repository/            → Spring Data JPA repositories
│       ├── security/              → JWT filter, token provider, UserDetailsService
│       └── service/               → Business logic & validation
├── frontend/                      → React SPA
│   └── src/
│       ├── components/            → Reusable UI (AppLayout, Sidebar, Modal, Toast)
│       ├── context/               → AuthContext (login / register / logout)
│       ├── hooks/                 → Custom hooks (useNotificationToast)
│       ├── pages/                 → Page components (Dashboard, Workouts, Sleep, …)
│       ├── services/              → API wrapper with automatic JWT injection
│       └── types.ts               → Shared TypeScript interfaces
└── README.md                      ← you are here
```

---

## Prerequisites

Make sure you have the following installed before continuing:

| Tool               | Version / Note                                  |
| ------------------ | ----------------------------------------------- |
| **Docker Desktop** | Required — runs the PostgreSQL database         |
| **Java 17**        | e.g. Eclipse Temurin                            |
| **Node.js**        | Version 20 or later                             |
| **IntelliJ IDEA**  | Used to run the backend (Community or Ultimate) |

> Maven does **not** need to be installed separately — the backend includes the Maven Wrapper (`mvnw`).

---

## How to Run the Application

The application consists of three parts that must be started **in order**:

1. Database (Docker)
2. Backend (IntelliJ IDEA)
3. Frontend (terminal — `npm run dev`)

### Step 1 — Start the Database

PostgreSQL runs inside a Docker container. Open a terminal and run:

**First time — create the container:**

```bash
docker run -d --name healthtracker-db \
  -e POSTGRES_DB=healthtracker \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  postgres:16
```

**Every time after that — start the existing container:**

```bash
docker start healthtracker-db
```

You can verify the container is running with:

```bash
docker ps
```

> The database tables are created automatically by the backend the first time it connects (Hibernate `ddl-auto=update`).

### Step 2 — Start the Backend

Open the **`backend`** folder in **IntelliJ IDEA**.

1. IntelliJ will detect the Maven project and import dependencies automatically.
2. Make sure the project SDK is set to **Java 17** (File → Project Structure → SDK).
3. Navigate to `src/main/java/com/gullberg/healthtracker/HealthTrackerSpringApplication.java`.
4. Right-click the file → **Run 'HealthTrackerSpringApplication'**.

The backend starts at **http://localhost:8080**.

<details>
<summary>Alternative: run from terminal</summary>

```bash
cd backend

# macOS / Linux
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
./mvnw spring-boot:run

# Windows (PowerShell)
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17"
.\mvnw.cmd spring-boot:run
```

</details>

### Step 3 — Start the Frontend

Open a terminal (for example the integrated terminal in IntelliJ IDEA or VS Code) and run:

```bash
cd frontend
npm install    # first time only — installs dependencies
npm run dev
```

The frontend starts at **http://localhost:5173**.

> **Important:** The backend must already be running before the frontend can be used.

### Step 4 — Use the Application

1. Open **http://localhost:5173** in a web browser.
2. Click **Register** to create a new account.
3. Log in and start adding entries for workouts, sleep, nutrition, and wellbeing.
4. Navigate to the **Dashboard** to see a daily summary.

---

## Configuration

All backend settings are in `backend/src/main/resources/application.properties`.

| Setting           | Default value                                    |
| ----------------- | ------------------------------------------------ |
| Database URL      | `jdbc:postgresql://localhost:5432/healthtracker` |
| Database user     | `postgres`                                       |
| Database password | `postgres`                                       |
| Server port       | `8080`                                           |
| JWT expiration    | `86400000` ms (24 hours)                         |

The frontend API base URL is set to `http://localhost:8080/api` in `frontend/src/services/api.ts`.  
CORS on the backend is configured to allow `http://localhost:5173`.

> These values are set for local development. If the application were to be deployed, the API URL, CORS origin, and JWT secret would need to be updated.

---

## Testing

### Backend — Unit Tests

The backend has unit tests for the service layer (JUnit 5, Mockito, AssertJ).

**In IntelliJ IDEA:**  
Right-click the `src/test` folder → **Run All Tests**.

**From terminal:**

```bash
cd backend

# macOS / Linux
JAVA_HOME=$(/usr/libexec/java_home -v 17) ./mvnw test

# Windows (PowerShell)
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17"
.\mvnw.cmd test
```

### Frontend — Linting

```bash
cd frontend
npm run lint
```

---

## Build for Production

### Backend

In IntelliJ IDEA: Maven tool window → Lifecycle → `package`.  
Or from terminal:

```bash
cd backend
JAVA_HOME=$(/usr/libexec/java_home -v 17) ./mvnw clean package
```

The resulting JAR is in `backend/target/`.

### Frontend

```bash
cd frontend
npm run build
```

The production build is written to `frontend/dist/`.

---

## API Endpoints

All endpoints except `/api/auth/**` require a valid JWT in the `Authorization: Bearer <token>` header.

### Authentication

| Method | Endpoint             | Description              |
| ------ | -------------------- | ------------------------ |
| POST   | `/api/auth/register` | Register a new user      |
| POST   | `/api/auth/login`    | Log in and receive a JWT |

### Workouts

| Method | Endpoint             | Description      |
| ------ | -------------------- | ---------------- |
| GET    | `/api/workouts`      | Get all workouts |
| GET    | `/api/workouts/{id}` | Get one workout  |
| POST   | `/api/workouts`      | Create a workout |
| PUT    | `/api/workouts/{id}` | Update a workout |
| DELETE | `/api/workouts/{id}` | Delete a workout |

### Sleep Logs

| Method | Endpoint               | Description        |
| ------ | ---------------------- | ------------------ |
| GET    | `/api/sleep-logs`      | Get all sleep logs |
| GET    | `/api/sleep-logs/{id}` | Get one sleep log  |
| POST   | `/api/sleep-logs`      | Create a sleep log |
| PUT    | `/api/sleep-logs/{id}` | Update a sleep log |
| DELETE | `/api/sleep-logs/{id}` | Delete a sleep log |

### Nutrition Logs

| Method | Endpoint                   | Description            |
| ------ | -------------------------- | ---------------------- |
| GET    | `/api/nutrition-logs`      | Get all nutrition logs |
| GET    | `/api/nutrition-logs/{id}` | Get one nutrition log  |
| POST   | `/api/nutrition-logs`      | Create a nutrition log |
| PUT    | `/api/nutrition-logs/{id}` | Update a nutrition log |
| DELETE | `/api/nutrition-logs/{id}` | Delete a nutrition log |

### Wellbeing Logs

| Method | Endpoint                   | Description            |
| ------ | -------------------------- | ---------------------- |
| GET    | `/api/wellbeing-logs`      | Get all wellbeing logs |
| GET    | `/api/wellbeing-logs/{id}` | Get one wellbeing log  |
| POST   | `/api/wellbeing-logs`      | Create a wellbeing log |
| PUT    | `/api/wellbeing-logs/{id}` | Update a wellbeing log |
| DELETE | `/api/wellbeing-logs/{id}` | Delete a wellbeing log |

### User

| Method | Endpoint        | Description          |
| ------ | --------------- | -------------------- |
| GET    | `/api/users/me` | Get the current user |

---

## Limitations

- The application is configured for **local development and examination only**.
- The frontend API URL and backend CORS are hardcoded for `localhost`.
- Frontend automated tests are not included in this version.
- No pagination — the API returns all entries per user in each request.

---

## Author

**Jakob Gullberg**  
Degree project (Examensarbete), Java24
