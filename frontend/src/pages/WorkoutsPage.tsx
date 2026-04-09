import { useEffect, useState } from "react";
import { api } from "../services/api";
import Modal from "../components/Modal";
import type { WorkoutResponse, WorkoutForm } from "../types";
import "./DataPage.css";

const TYPES = [
  "RUNNING",
  "CYCLING",
  "SWIMMING",
  "WEIGHT_TRAINING",
  "YOGA",
  "WALKING",
  "HIIT",
  "SPORTS",
  "OTHER",
];

const formatType = (t: string) =>
  t.replace(/_/g, " ").replace(/\b\w/g, (c) => c.toUpperCase());

const emptyForm: WorkoutForm = {
  workoutType: "RUNNING",
  durationMinutes: 30,
  caloriesBurned: "",
  notes: "",
  date: new Date().toISOString().split("T")[0],
};

export default function WorkoutsPage() {
  const [workouts, setWorkouts] = useState<WorkoutResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [editing, setEditing] = useState<WorkoutResponse | null>(null);
  const [form, setForm] = useState<WorkoutForm>(emptyForm);

  const fetchWorkouts = async () => {
    try {
      const data = await api.get<WorkoutResponse[]>("/workouts");
      setWorkouts(
        data.sort((a, b) => b.date.localeCompare(a.date) || b.id - a.id),
      );
    } catch {
      // silent
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchWorkouts();
  }, []);

  const openAdd = () => {
    setEditing(null);
    setForm(emptyForm);
    setModalOpen(true);
  };

  const openEdit = (w: WorkoutResponse) => {
    setEditing(w);
    setForm({
      workoutType: w.workoutType,
      durationMinutes: w.durationMinutes,
      caloriesBurned: w.caloriesBurned?.toString() ?? "",
      notes: w.notes ?? "",
      date: w.date,
    });
    setModalOpen(true);
  };

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    const body = {
      workoutType: form.workoutType,
      durationMinutes: form.durationMinutes,
      caloriesBurned: form.caloriesBurned ? Number(form.caloriesBurned) : null,
      notes: form.notes || null,
      date: form.date,
    };

    if (editing) {
      await api.put(`/workouts/${editing.id}`, body);
    } else {
      await api.post("/workouts", body);
    }

    setModalOpen(false);
    fetchWorkouts();
  };

  const handleDelete = async (id: number) => {
    await api.delete(`/workouts/${id}`);
    fetchWorkouts();
  };

  if (loading) {
    return <p className="empty-state">Loading workouts...</p>;
  }

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">Workouts</h1>
        <button className="btn btn--primary" onClick={openAdd}>
          + Add Workout
        </button>
      </div>

      {workouts.length === 0 ? (
        <p className="empty-state">
          No workouts logged yet. Click "Add Workout" to get started!
        </p>
      ) : (
        <div className="data-table-wrapper">
          <table className="data-table">
            <thead>
              <tr>
                <th>Date</th>
                <th>Type</th>
                <th>Duration</th>
                <th>Calories</th>
                <th>Notes</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {workouts.map((w) => (
                <tr key={w.id}>
                  <td>{w.date}</td>
                  <td>{formatType(w.workoutType)}</td>
                  <td>{w.durationMinutes} min</td>
                  <td>{w.caloriesBurned ?? "—"}</td>
                  <td className="cell-truncate">{w.notes || "—"}</td>
                  <td>
                    <div className="cell-actions">
                      <button
                        className="btn btn--ghost"
                        onClick={() => openEdit(w)}
                        title="Edit"
                      >
                        ✏️
                      </button>
                      <button
                        className="btn btn--danger"
                        onClick={() => handleDelete(w.id)}
                        title="Delete"
                      >
                        🗑️
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      <Modal
        open={modalOpen}
        onClose={() => setModalOpen(false)}
        title={editing ? "Edit Workout" : "Add Workout"}
      >
        <form className="form-stack" onSubmit={handleSave}>
          <div className="form-group">
            <label htmlFor="date">Date</label>
            <input
              id="date"
              type="date"
              value={form.date}
              max={new Date().toISOString().split("T")[0]}
              onChange={(e) => setForm({ ...form, date: e.target.value })}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="workoutType">Type</label>
            <select
              id="workoutType"
              value={form.workoutType}
              onChange={(e) =>
                setForm({ ...form, workoutType: e.target.value })
              }
            >
              {TYPES.map((t) => (
                <option key={t} value={t}>
                  {formatType(t)}
                </option>
              ))}
            </select>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="duration">Duration (min)</label>
              <input
                id="duration"
                type="number"
                min={1}
                value={form.durationMinutes}
                onChange={(e) =>
                  setForm({ ...form, durationMinutes: Number(e.target.value) })
                }
                required
              />
            </div>
            <div className="form-group">
              <label htmlFor="calories">Calories (optional)</label>
              <input
                id="calories"
                type="number"
                min={0}
                value={form.caloriesBurned}
                onChange={(e) =>
                  setForm({ ...form, caloriesBurned: e.target.value })
                }
              />
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="notes">Notes (optional)</label>
            <textarea
              id="notes"
              value={form.notes}
              onChange={(e) => setForm({ ...form, notes: e.target.value })}
            />
          </div>

          <button type="submit" className="btn btn--primary form-submit">
            Save
          </button>
        </form>
      </Modal>
    </div>
  );
}
