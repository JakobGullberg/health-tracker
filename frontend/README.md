# Health Tracker — Frontend

React 19 + TypeScript frontend for the Health Tracker application.

## Tech

- **React 19** with TypeScript
- **Vite 8** for dev server and bundling
- **React Router DOM** for client-side routing
- **Custom CSS** — no UI library, uses CSS custom properties for theming

## Getting Started

```bash
npm install    # install dependencies (first time only)
npm run dev    # start dev server at http://localhost:5173
```

## Build

```bash
npm run build  # outputs to dist/
npm run preview # preview production build locally
```

## Project Structure

```
src/
├── main.tsx                → App entry point
├── App.tsx                 → Router setup with ProtectedRoute/PublicRoute
├── index.css               → CSS reset + custom properties
├── types.ts                → Shared TypeScript interfaces
├── services/
│   └── api.ts              → fetch wrapper with auto JWT injection
├── context/
│   └── AuthContext.tsx      → Login/register/logout + token management
├── hooks/
│   └── useNotificationToast.ts → Success/error toast notifications
├── components/
│   ├── AppLayout/          → Sidebar + main content wrapper
│   ├── Sidebar/            → Navigation links + user info
│   ├── Modal/              → Reusable modal (ESC-close, click-outside)
│   └── NotificationToast/  → Toast notification component
└── pages/
    ├── LoginPage.tsx        → Login/register form
    ├── DashboardPage.tsx    → Date picker + summary cards
    ├── WorkoutsPage.tsx     → Workout CRUD table + form
    ├── SleepPage.tsx        → Sleep log CRUD table + form
    ├── NutritionPage.tsx    → Nutrition log CRUD table + form
    ├── WellbeingPage.tsx    → Wellbeing log CRUD (range sliders)
    └── DataPage.css         → Shared CSS for all CRUD pages
```

## Environment

The API base URL is set to `http://localhost:8080/api` in `src/services/api.ts`.  
Make sure the backend is running before starting the frontend.
