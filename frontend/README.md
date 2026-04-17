# Health Tracker — Frontend

React + TypeScript SPA for the Health Tracker application.

For full setup instructions, prerequisites, and how to run the complete application, see the [main README](../README.md) in the project root.

## Quick Reference

- **Framework:** React 19, TypeScript, Vite 8
- **Routing:** React Router DOM
- **Styling:** Custom CSS (no UI library)

## Run

Open a terminal in the `frontend/` folder and run:

```bash
npm install    # first time only
npm run dev
```

See [main README — Step 3](../README.md#step-3--start-the-frontend) for detailed instructions.

## Lint

```bash
npm run lint
```

## Build

```bash
npm run build      # output in dist/
npm run preview    # preview production build locally
```

## Project Structure

```
src/
├── main.tsx                    → Application entry point
├── App.tsx                     → Router setup and route guards
├── index.css                   → Global styles and design tokens
├── types.ts                    → Shared TypeScript interfaces
├── services/
│   └── api.ts                  → Fetch wrapper with automatic JWT injection
├── context/
│   └── AuthContext.tsx         → Authentication state and actions
├── hooks/
│   └── useNotificationToast.ts → Toast state management
├── components/
│   ├── AppLayout/              → Shared layout shell
│   ├── Sidebar/                → Navigation and user panel
│   ├── Modal/                  → Reusable modal component
│   └── NotificationToast/      → Toast UI component
└── pages/
    ├── LoginPage.tsx           → Login and register page
    ├── DashboardPage.tsx       → Daily summary overview
    ├── WorkoutsPage.tsx        → Workout CRUD page
    ├── SleepPage.tsx           → Sleep CRUD page
    ├── NutritionPage.tsx       → Nutrition CRUD page
    ├── WellbeingPage.tsx       → Wellbeing CRUD page
    └── DataPage.css            → Shared page styling for CRUD views
```

## Notes

- The API base URL is set to `http://localhost:8080/api` in `src/services/api.ts`.
- The backend must be running before the frontend can be used.
- Login state is stored in `localStorage`.
