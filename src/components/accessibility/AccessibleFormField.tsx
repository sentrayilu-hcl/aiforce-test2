import React, { InputHTMLAttributes } from 'react';

export interface AccessibleFormFieldProps extends InputHTMLAttributes<HTMLInputElement> {
  id: string;
  label: string;
  error?: string;
  hint?: string;
}

export function AccessibleFormField({ id, label, error, hint, ...rest }: AccessibleFormFieldProps) {
  const hintId = hint ? `${id}-hint` : undefined;
  const errorId = error ? `${id}-error` : undefined;
  const describedBy = [hintId, errorId].filter(Boolean).join(' ') || undefined;

  return (
    <div className="form-field">
      <label htmlFor={id}>{label}</label>
      {hint ? (
        <p id={hintId} className="form-hint">
          {hint}
        </p>
      ) : null}
      <input
        id={id}
        aria-invalid={Boolean(error)}
        aria-describedby={describedBy}
        {...rest}
      />
      {error ? (
        <p id={errorId} className="form-error" role="alert">
          {error}
        </p>
      ) : null}
    </div>
  );
}
