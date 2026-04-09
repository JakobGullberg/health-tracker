import { useEffect, useState } from "react";
import { api } from "../services/api";
import Modal from "../components/Modal";
import type { NutritionLogResponse, NutritionForm } from "../types";
import "./DataPage.css";

const MEAL_TYPES = ["BREAKFAST", "LUNCH", "DINNER", "SNACK"];

const formatMealType = (t: string) => t.charAt(0) + t.slice(1).toLowerCase();

const emptyForm: NutritionForm = {
  mealType: "BREAKFAST",
  description: "",
  calories: "",
  proteinGrams: "",
  carbsGrams: "",
  fatGrams: "",
  date: new Date().toISOString().split("T")[0],
};

export default function NutritionPage() {
  const [logs, setLogs] = useState<NutritionLogResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [editing, setEditing] = useState<NutritionLogResponse | null>(null);
  const [form, setForm] = useState<NutritionForm>(emptyForm);

  const fetchLogs = async () => {
    try {
      const data = await api.get<NutritionLogResponse[]>("/nutrition-logs");
      setLogs(data.sort((a, b) => b.date.localeCompare(a.date) || b.id - a.id));
    } catch {
      // silent
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchLogs();
  }, []);

  const openAdd = () => {
    setEditing(null);
    setForm(emptyForm);
    setModalOpen(true);
  };

  const openEdit = (n: NutritionLogResponse) => {
    setEditing(n);
    setForm({
      mealType: n.mealType,
      description: n.description,
      calories: n.calories?.toString() ?? "",
      proteinGrams: n.proteinGrams?.toString() ?? "",
      carbsGrams: n.carbsGrams?.toString() ?? "",
      fatGrams: n.fatGrams?.toString() ?? "",
      date: n.date,
    });
    setModalOpen(true);
  };

  const toNum = (v: string) => (v ? Number(v) : null);

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    const body = {
      mealType: form.mealType,
      description: form.description,
      calories: toNum(form.calories),
      proteinGrams: toNum(form.proteinGrams),
      carbsGrams: toNum(form.carbsGrams),
      fatGrams: toNum(form.fatGrams),
      date: form.date,
    };

    if (editing) {
      await api.put(`/nutrition-logs/${editing.id}`, body);
    } else {
      await api.post("/nutrition-logs", body);
    }

    setModalOpen(false);
    fetchLogs();
  };

  const handleDelete = async (id: number) => {
    await api.delete(`/nutrition-logs/${id}`);
    fetchLogs();
  };

  if (loading) {
    return <p className="empty-state">Loading nutrition logs...</p>;
  }

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">Nutrition</h1>
        <button className="btn btn--primary" onClick={openAdd}>
          + Add Meal
        </button>
      </div>

      {logs.length === 0 ? (
        <p className="empty-state">
          No meals logged yet. Click "Add Meal" to start tracking!
        </p>
      ) : (
        <div className="data-table-wrapper">
          <table className="data-table">
            <thead>
              <tr>
                <th>Date</th>
                <th>Meal</th>
                <th>Description</th>
                <th>Calories</th>
                <th>P / C / F</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {logs.map((n) => (
                <tr key={n.id}>
                  <td>{n.date}</td>
                  <td>{formatMealType(n.mealType)}</td>
                  <td className="cell-truncate">{n.description}</td>
                  <td>{n.calories ?? "—"}</td>
                  <td>
                    {[n.proteinGrams, n.carbsGrams, n.fatGrams]
                      .map((v) => v ?? "—")
                      .join(" / ")}
                    g
                  </td>
                  <td>
                    <div className="cell-actions">
                      <button
                        className="btn btn--ghost"
                        onClick={() => openEdit(n)}
                        title="Edit"
                      >
                        ✏️
                      </button>
                      <button
                        className="btn btn--danger"
                        onClick={() => handleDelete(n.id)}
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
        title={editing ? "Edit Meal" : "Add Meal"}
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
            <label htmlFor="mealType">Meal Type</label>
            <select
              id="mealType"
              value={form.mealType}
              onChange={(e) => setForm({ ...form, mealType: e.target.value })}
            >
              {MEAL_TYPES.map((t) => (
                <option key={t} value={t}>
                  {formatMealType(t)}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label htmlFor="description">Description</label>
            <input
              id="description"
              type="text"
              value={form.description}
              onChange={(e) =>
                setForm({ ...form, description: e.target.value })
              }
              required
            />
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="calories">Calories</label>
              <input
                id="calories"
                type="number"
                min={0}
                value={form.calories}
                onChange={(e) => setForm({ ...form, calories: e.target.value })}
              />
            </div>
            <div className="form-group">
              <label htmlFor="protein">Protein (g)</label>
              <input
                id="protein"
                type="number"
                min={0}
                value={form.proteinGrams}
                onChange={(e) =>
                  setForm({ ...form, proteinGrams: e.target.value })
                }
              />
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="carbs">Carbs (g)</label>
              <input
                id="carbs"
                type="number"
                min={0}
                value={form.carbsGrams}
                onChange={(e) =>
                  setForm({ ...form, carbsGrams: e.target.value })
                }
              />
            </div>
            <div className="form-group">
              <label htmlFor="fat">Fat (g)</label>
              <input
                id="fat"
                type="number"
                min={0}
                value={form.fatGrams}
                onChange={(e) => setForm({ ...form, fatGrams: e.target.value })}
              />
            </div>
          </div>

          <button type="submit" className="btn btn--primary form-submit">
            Save
          </button>
        </form>
      </Modal>
    </div>
  );
}
