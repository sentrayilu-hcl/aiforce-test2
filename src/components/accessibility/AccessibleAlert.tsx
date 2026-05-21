import React from 'react';

export interface AccessibleAlertProps {
  title?: string;
  children: React.ReactNode;
  tone?: 'error' | 'info' | 'success' | 'warning';
}

export function AccessibleAlert({
  title,
  children,
  tone = 'info'
}: AccessibleAlertProps) {
  return (
    <div role={tone === 'error' ? 'alert' : 'status'} aria-live={tone === 'error' ? 'assertive' : 'polite'} className={`alert alert-${tone}`}>
      {title ? <strong>{title}</strong> : null}
      <div>{children}</div>
    </div>
  );
}
