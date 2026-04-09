import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { api } from "../services/api";
import type {
  WorkoutResponse,
  SleepLogResponse,
  NutritionLogResponse,
  WellbeingLogResponse,
} from "../types";
import "./DashboardPage.css";

function getGreeting(): string {
  const hour = new Date().getHours();
  if (hour < 12) return "Good morning";
  if (hour < 17) return "Good afternoon";
  return "Good evening";
}

export default function DashboardPage() {
  const { user } = useAuth();
  const navigate = useNavigate();

  const [workouts, setWorkouts] = useState<WorkoutResponse[]>([]);
  const [sleepLogs, setSleepLogs] = useState<SleepLogResponse[]>([]);
  const [nutritionLogs, setNutritionLogs] = useState<NutritionLogResponse[]>(
    [],
  );
  const [wellbeingLogs, setWellbeingLogs] = useState<WellbeingLogResponse[]>(
    [],
  );
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchData() {
      try {
        const [w, s, n, wb] = await Promise.all([
          api.get<WorkoutResponse[]>("/workouts"),
          api.get<SleepLogResponse[]>("/sleep-logs"),
          api.get<NutritionLogResponse[]>("/nutrition-logs"),
          api.get<WellbeingLogResponse[]>("/wellbeing-logs"),
        ]);
        setWorkouts(w);
        setSleepLogs(s);
        setNutritionLogs(n);
        setWellbeingLogs(wb);
      } catch {
        // silently fail — cards will show empty state
      } finally {
        setLoading(false);
      }
    }
    fetchData();
  }, []);

  if (loading) {
    return <p className="dashboard-loading">Loading your data...</p>;
  }

  const today = new Date().toISOString().split("T")[0];

  // Workouts summary
  const startOfWeek = new Date();
  startOfWeek.setDate(startOfWeek.getDate() - startOfWeek.getDay());
  const weekStr = startOfWeek.toISOString().split("T")[0];
  const weekWorkouts = workouts.filter((w) => w.date >= weekStr);
  const latestWorkout = workouts.length > 0 ? workouts[0] : null;

  // Sleep summary
  const latestSleep = sleepLogs.length > 0 ? sleepLogs[0] : null;
  const avgQuality =
    sleepLogs.length > 0
      ? (
          sleepLogs.slice(0, 7).reduce((a, s) => a + s.sleepQuality, 0) /
          Math.min(sleepLogs.length, 7)
        ).toFixed(1)
      : null;

  // Nutrition summary
  const todayMeals = nutritionLogs.filter((n) => n.date === today);
  const todayCalories = todayMeals.reduce((a, n) => a + (n.calories || 0), 0);
  const latestMeal = nutritionLogs.length > 0 ? nutritionLogs[0] : null;

  // Wellbeing summary
  const todayWellbeing = wellbeingLogs.find((w) => w.date === today) || null;

  return (
    <div className="dashboard">
      <h1 className="dashboard-greeting">
        {getGreeting()}, {user?.firstName}!
      </h1>

      <div className="dashboard-grid">
        {/* Workouts */}
        <div className="summary-card" onClick={() => navigate("/workouts")}>
          <div className="summary-card-header">
            <div className="summary-card-icon summary-card-icon--workout">
              🏋️
            </div>
            <span className="summary-card-title">Workouts</span>
          </div>
          {latestWorkout ? (
            <>
              <p className="summary-card-detail">
                Latest: {latestWorkout.workoutType} —{" "}
                {latestWorkout.durationMinutes} min
              </p>
              <p className="summary-card-stat">
                {weekWorkouts.length} this week
              </p>
            </>
          ) : (
            <p className="summary-card-detail">No workouts logged yet</p>
          )}
        </div>

        {/* Sleep */}
        <div className="summary-card" onClick={() => navigate("/sleep")}>
          <div className="summary-card-header">
            <div className="summary-card-icon summary-card-icon--sleep">🌙</div>
            <span className="summary-card-title">Sleep</span>
          </div>
          {latestSleep ? (
            <>
              <p className="summary-card-detail">
                Latest: {latestSleep.durationHours}h — Quality{" "}
                {latestSleep.sleepQuality}/10
              </p>
              <p className="summary-card-stat">Avg quality: {avgQuality}/10</p>
            </>
          ) : (
            <p className="summary-card-detail">No sleep logs yet</p>
          )}
        </div>

        {/* Nutrition */}
        <div className="summary-card" onClick={() => navigate("/nutrition")}>
          <div className="summary-card-header">
            <div className="summary-card-icon summary-card-icon--nutrition">
              🍎
            </div>
            <span className="summary-card-title">Nutrition</span>
          </div>
          {latestMeal ? (
            <>
              <p className="summary-card-detail">
                Latest: {latestMeal.mealType} — {latestMeal.description}
              </p>
              <p className="summary-card-stat">{todayCalories} kcal today</p>
            </>
          ) : (
            <p className="summary-card-detail">No meals logged yet</p>
          )}
        </div>

        {/* Wellbeing */}
        <div className="summary-card" onClick={() => navigate("/wellbeing")}>
          <div className="summary-card-header">
            <div className="summary-card-icon summary-card-icon--wellbeing">
              🧠
            </div>
            <span className="summary-card-title">Wellbeing</span>
          </div>
          {todayWellbeing ? (
            <p className="summary-card-detail">
              Mood {todayWellbeing.moodRating}/10 · Stress{" "}
              {todayWellbeing.stressLevel}/10 · Energy{" "}
              {todayWellbeing.energyLevel}/10
            </p>
          ) : (
            <p className="summary-card-detail">No check-in today</p>
          )}
        </div>
      </div>
    </div>
  );
}
