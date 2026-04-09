import { useState, useCallback, useRef } from "react";

export type NotificationType = "success" | "error";

export interface Notification {
  message: string;
  type: NotificationType;
}

export function useNotificationToast(autoDismissMs = 4000) {
  const [notification, setNotification] = useState<Notification | null>(null);
  const timerRef = useRef<ReturnType<typeof setTimeout> | null>(null);

  const show = useCallback(
    (message: string, type: NotificationType) => {
      if (timerRef.current) clearTimeout(timerRef.current);
      setNotification({ message, type });
      timerRef.current = setTimeout(() => setNotification(null), autoDismissMs);
    },
    [autoDismissMs],
  );

  const showSuccess = useCallback(
    (message: string) => show(message, "success"),
    [show],
  );

  const showError = useCallback(
    (message: string) => show(message, "error"),
    [show],
  );

  const dismiss = useCallback(() => {
    if (timerRef.current) clearTimeout(timerRef.current);
    setNotification(null);
  }, []);

  return { notification, showSuccess, showError, dismiss };
}
