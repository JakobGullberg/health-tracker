import { useEffect, useState } from "react";
import { api } from "../services/api";
import Modal from "../components/Modal";
import type { WellbeingLogResponse, WellbeingForm } from "../types";
import "./DataPage.css";

const emptyForm: WellbeingForm = {
  moodRating: 7,
  stressLevel: 5,
  energyLevel: 7,
  notes: "",
  date: new Date().toISOString().split("T")[0],
};

export default function WellbeingPage() {
  const [logs, setLogs] = useState<WellbeingLogResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [editing, setEditing] = useState<WellbeingLogResponse | null>(null);
  const [form, setForm] = useState<WellbeingForm>(emptyForm);

  const fetchLogs = async () => {
    try {
      const data = await api.get<WellbeingLogResponse[]>("/wellbeing-logs");
      setLogs(data);
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

  const openEdit = (w: WellbeingLogResponse) => {
    setEditing(w);
    setForm({
      moodRating: w.moodRating,
      stressLevel: w.stressLevel,
      energyLevel: w.energyLevel,
      notes: w.notes ?? "",
      date: w.date,
    });
    setModalOpen(true);
  };

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    const body = {
      moodRating: form.moodRating,
      stressLevel: form.stressLevel,
      energyLevel: form.energyLevel,
      notes: form.notes || null,
      date: form.date,
    };

    if (editing) {
      await api.put(`/wellbeing-logs/${editing.id}`, body);
    } else {
      await api.post("/wellbeing-logs", body);
    }

    setModalOpen(false);
    fetchLogs();
  };

  const handleDelete = async (id: number) => {
    await api.delete(`/wellbeing-logs/${id}`);
    fetchLogs();
  };

  if (loading) {
    return <p className="empty-state">Loading wellbeing logs...</p>;
  }

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">Wellbeing</h1>
        <button className="btn btn--primary" onClick={openAdd}>
          + Add Check-in
        </button>
      </div>

      {logs.length === 0 ? (
        <p className="empty-state">
          No check-ins yet. Click "Add Check-in" to start tracking!
        </p>
      ) : (
        <div className="data-table-wrapper">
          <table className="data-table">
            <thead>
              <tr>
                <th>Date</th>
                <th>Mood</th>
                <th>Stress</th>
                <th>Energy</th>
                <th>Notes</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {logs.map((w) => (
                <tr key={w.id}>
                  <td>{w.date}</td>
                  <td>{w.moodRating}/10</td>
                  <td>{w.stressLevel}/10</td>
                  <td>{w.energyLevel}/10</td>
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
        title={editing ? "Edit Check-in" : "Add Check-in"}
      >
        <form className="form-stack" onSubmit={handleSave}>
          <div className="form-group">
            <label htmlFor="date">Date</label>
            <input
              id="date"
              type="date"
              value={form.date}
              onChange={(e) => setForm({ ...form, date: e.target.value })}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="mood">Mood (1–10): {form.moodRating}</label>
            <input
              id="mood"
              type="range"
              min={1}
              max={10}
              value={form.moodRating}
              onChange={(e) =>
                setForm({ ...form, moodRating: Number(e.target.value) })
              }
            />
          </div>

          <div className="form-group">
            <label htmlFor="stress">Stress (1–10): {form.stressLevel}</label>
            <input
              id="stress"
              type="range"
              min={1}
              max={10}
              value={form.stressLevel}
              onChange={(e) =>
                setForm({ ...form, stressLevel: Number(e.target.value) })
              }
            />
          </div>

          <div className="form-group">
            <label htmlFor="energy">Energy (1–10): {form.energyLevel}</label>
            <input
              id="energy"
              type="range"
              min={1}
              max={10}
              value={form.energyLevel}
              onChange={(e) =>
                setForm({ ...form, energyLevel: Number(e.target.value) })
              }
            />
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
