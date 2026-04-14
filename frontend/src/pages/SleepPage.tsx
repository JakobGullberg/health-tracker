import { useEffect, useState } from "react";
import { api } from "../services/api";
import Modal from "../components/Modal/Modal";
import NotificationToast from "../components/NotificationToast/NotificationToast";
import { useNotificationToast } from "../hooks/useNotificationToast";
import type { SleepLogResponse, SleepForm } from "../types";
import "./DataPage.css";

const emptyForm: SleepForm = {
  bedtime: "23:00",
  wakeTime: "07:00",
  durationHours: 8,
  sleepQuality: 7,
  notes: "",
  date: new Date().toISOString().split("T")[0],
};

export default function SleepPage() {
  const [sleepLogs, setSleepLogs] = useState<SleepLogResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [editing, setEditing] = useState<SleepLogResponse | null>(null);
  const [form, setForm] = useState<SleepForm>(emptyForm);
  const { notification, showSuccess, showError, dismiss } =
    useNotificationToast();

  /** Calculate sleep duration in hours from bedtime → wakeTime (handles overnight). */
  const calcDuration = (bedtime: string, wakeTime: string): number => {
    const [bh, bm] = bedtime.split(":").map(Number);
    const [wh, wm] = wakeTime.split(":").map(Number);
    let mins = wh * 60 + wm - (bh * 60 + bm);
    if (mins <= 0) mins += 24 * 60; // overnight
    return Math.round((mins / 60) * 2) / 2; // round to nearest 0.5
  };

  const fetchSleepLogs = async () => {
    try {
      const data = await api.get<SleepLogResponse[]>("/sleep-logs");
      setSleepLogs(
        data.sort((a, b) => b.date.localeCompare(a.date) || b.id - a.id),
      );
    } catch {
      // silent
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchSleepLogs();
  }, []);

  const openAdd = () => {
    setEditing(null);
    setForm(emptyForm);
    setModalOpen(true);
  };

  const openEdit = (s: SleepLogResponse) => {
    setEditing(s);
    setForm({
      bedtime: s.bedtime,
      wakeTime: s.wakeTime,
      durationHours: s.durationHours,
      sleepQuality: s.sleepQuality,
      notes: s.notes ?? "",
      date: s.date,
    });
    setModalOpen(true);
  };

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    const body = {
      bedtime: form.bedtime,
      wakeTime: form.wakeTime,
      durationHours: form.durationHours,
      sleepQuality: form.sleepQuality,
      notes: form.notes || null,
      date: form.date,
    };

    try {
      if (editing) {
        await api.put(`/sleep-logs/${editing.id}`, body);
        showSuccess("Sleep log updated successfully.");
      } else {
        await api.post("/sleep-logs", body);
        showSuccess("Sleep log added successfully.");
      }
      setModalOpen(false);
      fetchSleepLogs();
    } catch (err) {
      showError(
        err instanceof Error ? err.message : "Failed to save sleep log.",
      );
    }
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm("Are you sure you want to delete this sleep log?"))
      return;
    try {
      await api.delete(`/sleep-logs/${id}`);
      showSuccess("Sleep log deleted.");
      fetchSleepLogs();
    } catch (err) {
      showError(
        err instanceof Error ? err.message : "Failed to delete sleep log.",
      );
    }
  };

  if (loading) {
    return <p className="empty-state">Loading sleep logs...</p>;
  }

  return (
    <div>
      <NotificationToast notification={notification} onDismiss={dismiss} />
      <div className="page-header">
        <h1 className="page-title">Sleep</h1>
        <button className="btn btn--primary" onClick={openAdd}>
          + Add Sleep Log
        </button>
      </div>

      {sleepLogs.length === 0 ? (
        <p className="empty-state">
          No sleep logs yet. Click "Add Sleep Log" to start tracking!
        </p>
      ) : (
        <div className="data-table-wrapper">
          <table className="data-table">
            <thead>
              <tr>
                <th>Date</th>
                <th>Bedtime</th>
                <th>Wake</th>
                <th>Duration</th>
                <th>Quality</th>
                <th>Notes</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {sleepLogs.map((s) => (
                <tr key={s.id}>
                  <td>{s.date}</td>
                  <td>{s.bedtime}</td>
                  <td>{s.wakeTime}</td>
                  <td>{s.durationHours}h</td>
                  <td>{s.sleepQuality}/10</td>
                  <td className="cell-truncate">{s.notes || "—"}</td>
                  <td>
                    <div className="cell-actions">
                      <button
                        className="btn btn--ghost"
                        onClick={() => openEdit(s)}
                        title="Edit"
                      >
                        ✏️
                      </button>
                      <button
                        className="btn btn--danger"
                        onClick={() => handleDelete(s.id)}
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
        title={editing ? "Edit Sleep Log" : "Add Sleep Log"}
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

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="bedtime">Bedtime</label>
              <input
                id="bedtime"
                type="time"
                value={form.bedtime}
                onChange={(e) => {
                  const bedtime = e.target.value;
                  setForm({
                    ...form,
                    bedtime,
                    durationHours: calcDuration(bedtime, form.wakeTime),
                  });
                }}
                required
              />
            </div>
            <div className="form-group">
              <label htmlFor="wakeTime">Wake Time</label>
              <input
                id="wakeTime"
                type="time"
                value={form.wakeTime}
                onChange={(e) => {
                  const wakeTime = e.target.value;
                  setForm({
                    ...form,
                    wakeTime,
                    durationHours: calcDuration(form.bedtime, wakeTime),
                  });
                }}
                required
              />
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="duration">Duration (auto)</label>
              <input
                id="duration"
                type="number"
                min={0}
                step={0.5}
                value={form.durationHours}
                readOnly
              />
            </div>
            <div className="form-group">
              <label htmlFor="quality">Quality (1–10)</label>
              <input
                id="quality"
                type="number"
                min={1}
                max={10}
                value={form.sleepQuality}
                onChange={(e) =>
                  setForm({ ...form, sleepQuality: Number(e.target.value) })
                }
                required
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
