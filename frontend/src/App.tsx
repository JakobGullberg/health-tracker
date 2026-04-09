import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider, useAuth } from "./context/AuthContext";
import AppLayout from "./components/AppLayout/AppLayout";
import LoginPage from "./pages/LoginPage";
import DashboardPage from "./pages/DashboardPage";
import WorkoutsPage from "./pages/WorkoutsPage";
import SleepPage from "./pages/SleepPage";
import NutritionPage from "./pages/NutritionPage";
import WellbeingPage from "./pages/WellbeingPage";

function ProtectedRoute({ children }: { children: React.ReactNode }) {
  const { user, loading } = useAuth();
  if (loading) return <div className="loading">Loading...</div>;
  if (!user) return <Navigate to="/login" replace />;
  return <>{children}</>;
}

function PublicRoute({ children }: { children: React.ReactNode }) {
  const { user, loading } = useAuth();
  if (loading) return <div className="loading">Loading...</div>;
  if (user) return <Navigate to="/" replace />;
  return <>{children}</>;
}

function AppRoutes() {
  return (
    <Routes>
      <Route
        path="/login"
        element={
          <PublicRoute>
            <LoginPage />
          </PublicRoute>
        }
      />
      <Route
        path="/"
        element={
          <ProtectedRoute>
            <AppLayout>
              <DashboardPage />
            </AppLayout>
          </ProtectedRoute>
        }
      />
      <Route
        path="/workouts"
        element={
          <ProtectedRoute>
            <AppLayout>
              <WorkoutsPage />
            </AppLayout>
          </ProtectedRoute>
        }
      />
      <Route
        path="/sleep"
        element={
          <ProtectedRoute>
            <AppLayout>
              <SleepPage />
            </AppLayout>
          </ProtectedRoute>
        }
      />
      <Route
        path="/nutrition"
        element={
          <ProtectedRoute>
            <AppLayout>
              <NutritionPage />
            </AppLayout>
          </ProtectedRoute>
        }
      />
      <Route
        path="/wellbeing"
        element={
          <ProtectedRoute>
            <AppLayout>
              <WellbeingPage />
            </AppLayout>
          </ProtectedRoute>
        }
      />
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <AppRoutes />
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;
