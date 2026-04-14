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

  const today = new Date().toISOString().split("T")[0];
  const [selectedDate, setSelectedDate] = useState(today);

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
        const byDateDesc = <T extends { date: string; id: number }>(
          a: T,
          b: T,
        ) => b.date.localeCompare(a.date) || b.id - a.id;
        setWorkouts(w.sort(byDateDesc));
        setSleepLogs(s.sort(byDateDesc));
        setNutritionLogs(n.sort(byDateDesc));
        setWellbeingLogs(wb.sort(byDateDesc));
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

  // Filter by selected date
  const dayWorkouts = workouts.filter((w) => w.date === selectedDate);
  const daySleep = sleepLogs.filter((s) => s.date === selectedDate);
  const dayNutrition = nutritionLogs.filter((n) => n.date === selectedDate);
  const dayWellbeing = wellbeingLogs.find((w) => w.date === selectedDate);

  // Nutrition totals for the day
  const dayCaloriesEaten = dayNutrition.reduce(
    (a, n) => a + (n.calories || 0),
    0,
  );

  // Workout calories burned for the day
  const dayCaloriesBurned = dayWorkouts.reduce(
    (a, w) => a + (w.caloriesBurned || 0),
    0,
  );

  // Date navigation helpers
  const goDay = (offset: number) => {
    const d = new Date(selectedDate);
    d.setDate(d.getDate() + offset);
    if (d.toISOString().split("T")[0] <= today) {
      setSelectedDate(d.toISOString().split("T")[0]);
    }
  };

  const isToday = selectedDate === today;

  // ── Weekly overview data ─────────────────────────────────
  const getWeekDays = (dateStr: string): string[] => {
    const d = new Date(dateStr);
    const day = d.getDay(); // 0=Sun
    const mon = new Date(d);
    mon.setDate(d.getDate() - ((day + 6) % 7)); // Monday
    return Array.from({ length: 7 }, (_, i) => {
      const date = new Date(mon);
      date.setDate(mon.getDate() + i);
      return date.toISOString().split("T")[0];
    });
  };

  const weekDays = getWeekDays(selectedDate);
  const dayLabels = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"];

  const weekCaloriesEaten = weekDays.map((d) =>
    nutritionLogs
      .filter((n) => n.date === d)
      .reduce((a, n) => a + (n.calories || 0), 0),
  );
  const weekCaloriesBurned = weekDays.map((d) =>
    workouts
      .filter((w) => w.date === d)
      .reduce((a, w) => a + (w.caloriesBurned || 0), 0),
  );
  const weekSleepHours = weekDays.map((d) => {
    const log = sleepLogs.find((s) => s.date === d);
    return log ? log.durationHours : 0;
  });
  const weekMood = weekDays.map((d) => {
    const log = wellbeingLogs.find((w) => w.date === d);
    return log ? log.moodRating : 0;
  });

  return (
    <div className="dashboard">
      <h1 className="dashboard-greeting">
        {getGreeting()}, {user?.firstName}!
      </h1>

      <div className="dashboard-date-picker">
        <button className="btn btn--ghost" onClick={() => goDay(-1)}>
          ←
        </button>
        <input
          type="date"
          value={selectedDate}
          max={today}
          onChange={(e) => setSelectedDate(e.target.value)}
          className="date-input"
        />
        <button
          className="btn btn--ghost"
          onClick={() => goDay(1)}
          disabled={isToday}
        >
          →
        </button>
        {!isToday && (
          <button
            className="btn btn--ghost"
            onClick={() => setSelectedDate(today)}
          >
            Today
          </button>
        )}
      </div>

      <div className="dashboard-grid">
        {/* Workouts */}
        <div className="summary-card" onClick={() => navigate("/workouts")}>
          <div className="summary-card-header">
            <div className="summary-card-icon summary-card-icon--workout">
              🏋️
            </div>
            <span className="summary-card-title">Workouts</span>
          </div>
          {dayWorkouts.length > 0 ? (
            <>
              <p className="summary-card-detail">
                {dayWorkouts.map((w) => w.workoutType).join(", ")}
              </p>
              <p className="summary-card-stat">
                {dayWorkouts.length} workout{dayWorkouts.length > 1 ? "s" : ""}{" "}
                · {dayWorkouts.reduce((a, w) => a + w.durationMinutes, 0)} min
              </p>
              <p className="summary-card-stat">
                {dayCaloriesBurned} kcal burned
              </p>
            </>
          ) : (
            <p className="summary-card-detail">No workouts this day</p>
          )}
        </div>

        {/* Sleep */}
        <div className="summary-card" onClick={() => navigate("/sleep")}>
          <div className="summary-card-header">
            <div className="summary-card-icon summary-card-icon--sleep">🌙</div>
            <span className="summary-card-title">Sleep</span>
          </div>
          {daySleep.length > 0 ? (
            <>
              <p className="summary-card-detail">
                {daySleep[0].durationHours}h — Quality{" "}
                {daySleep[0].sleepQuality}/10
              </p>
              <p className="summary-card-stat">
                {daySleep[0].bedtime} → {daySleep[0].wakeTime}
              </p>
            </>
          ) : (
            <p className="summary-card-detail">No sleep logged this day</p>
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
          {dayNutrition.length > 0 ? (
            <>
              <p className="summary-card-detail">
                {dayNutrition.length} meal{dayNutrition.length > 1 ? "s" : ""}{" "}
                logged
              </p>
              <p className="summary-card-stat">{dayCaloriesEaten} kcal</p>
            </>
          ) : (
            <p className="summary-card-detail">No meals logged this day</p>
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
          {dayWellbeing ? (
            <p className="summary-card-detail">
              Mood {dayWellbeing.moodRating}/10 · Stress{" "}
              {dayWellbeing.stressLevel}/10 · Energy {dayWellbeing.energyLevel}
              /10
            </p>
          ) : (
            <p className="summary-card-detail">No check-in this day</p>
          )}
        </div>

        {/* Calorie Balance */}
        <div className="summary-card summary-card--balance">
          <div className="summary-card-header">
            <div className="summary-card-icon summary-card-icon--balance">
              ⚖️
            </div>
            <span className="summary-card-title">Calorie Balance</span>
          </div>
          <div className="balance-rows">
            <p className="summary-card-detail">
              Eaten: <strong>{dayCaloriesEaten} kcal</strong>
            </p>
            <p className="summary-card-detail">
              Burned: <strong>{dayCaloriesBurned} kcal</strong>
            </p>
            <p className="summary-card-stat balance-net">
              Net: {dayCaloriesEaten - dayCaloriesBurned} kcal
            </p>
          </div>
        </div>
      </div>

      {/* ── Weekly Overview Charts ──────────────────────────── */}
      <h2 className="weekly-title">Weekly Overview</h2>
      <div className="weekly-grid">
        {/* Calories Chart */}
        <div className="chart-card">
          <h3 className="chart-card-title">Calories (kcal)</h3>
          <div className="chart">
            {weekDays.map((d, i) => {
              const eaten = weekCaloriesEaten[i];
              const burned = weekCaloriesBurned[i];
              const max = Math.max(
                ...weekCaloriesEaten,
                ...weekCaloriesBurned,
                1,
              );
              return (
                <div
                  key={d}
                  className={`chart-col ${d === selectedDate ? "chart-col--active" : ""}`}
                >
                  <div className="chart-bars">
                    <div
                      className="chart-bar chart-bar--eaten"
                      style={{ height: `${(eaten / max) * 100}%` }}
                      title={`Eaten: ${eaten} kcal`}
                    />
                    <div
                      className="chart-bar chart-bar--burned"
                      style={{ height: `${(burned / max) * 100}%` }}
                      title={`Burned: ${burned} kcal`}
                    />
                  </div>
                  <span className="chart-label">{dayLabels[i]}</span>
                </div>
              );
            })}
          </div>
          <div className="chart-legend">
            <span className="chart-legend-item">
              <span className="chart-dot chart-dot--eaten" /> Eaten
            </span>
            <span className="chart-legend-item">
              <span className="chart-dot chart-dot--burned" /> Burned
            </span>
          </div>
        </div>

        {/* Sleep Chart */}
        <div className="chart-card">
          <h3 className="chart-card-title">Sleep (hours)</h3>
          <div className="chart">
            {weekDays.map((d, i) => {
              const hours = weekSleepHours[i];
              const max = Math.max(...weekSleepHours, 1);
              return (
                <div
                  key={d}
                  className={`chart-col ${d === selectedDate ? "chart-col--active" : ""}`}
                >
                  <div className="chart-bars">
                    <div
                      className="chart-bar chart-bar--sleep"
                      style={{ height: `${(hours / max) * 100}%` }}
                      title={`${hours}h`}
                    />
                  </div>
                  <span className="chart-label">{dayLabels[i]}</span>
                </div>
              );
            })}
          </div>
        </div>

        {/* Mood Chart */}
        <div className="chart-card">
          <h3 className="chart-card-title">Mood (1–10)</h3>
          <div className="chart">
            {weekDays.map((d, i) => {
              const mood = weekMood[i];
              return (
                <div
                  key={d}
                  className={`chart-col ${d === selectedDate ? "chart-col--active" : ""}`}
                >
                  <div className="chart-bars">
                    <div
                      className="chart-bar chart-bar--mood"
                      style={{ height: `${(mood / 10) * 100}%` }}
                      title={mood > 0 ? `Mood: ${mood}/10` : "No data"}
                    />
                  </div>
                  <span className="chart-label">{dayLabels[i]}</span>
                </div>
              );
            })}
          </div>
        </div>
      </div>
    </div>
  );
}
