import type { Notification } from "../../hooks/useNotificationToast";
import "./NotificationToast.css";

interface NotificationProps {
  notification: Notification | null;
  onDismiss: () => void;
}

export default function NotificationToast({
  notification,
  onDismiss,
}: NotificationProps) {
  if (!notification) return null;

  return (
    <div className={`notification notification--${notification.type}`}>
      <span className="notification-message">{notification.message}</span>
      <button
        className="notification-close"
        onClick={onDismiss}
        aria-label="Dismiss"
      >
        ✕
      </button>
    </div>
  );
}
