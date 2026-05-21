import React from 'react';

export interface AccessibleToastProps {
  message: string;
  tone?: 'success' | 'error' | 'info';
  onDismiss?: () => void;
}

export function AccessibleToast({
  message,
  tone = 'info',
  onDismiss
}: AccessibleToastProps) {
  return (
    <div className={`toast toast-${tone}`} role="status" aria-live="polite" aria-atomic="true">
      <div>{message}</div>
      {onDismiss ? (
        <button type="button" aria-label="Dismiss notification" onClick={onDismiss}>
          Dismiss
        </button>
      ) : null}
    </div>
  );
}
