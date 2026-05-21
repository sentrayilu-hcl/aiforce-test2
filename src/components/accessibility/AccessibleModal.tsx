import React, { useEffect } from 'react';
import { useFocusManagement } from '../../hooks/accessibility/useFocusManagement';

export interface AccessibleModalProps {
  isOpen: boolean;
  title: string;
  children: React.ReactNode;
  onClose: () => void;
}

export function AccessibleModal({ isOpen, title, children, onClose }: AccessibleModalProps) {
  const { containerRef, storeActiveElement, restoreFocus } = useFocusManagement<HTMLDivElement>({
    shouldTrapFocus: true,
    restoreFocusOnUnmount: true
  });

  useEffect(() => {
    if (isOpen) storeActiveElement();
  }, [isOpen, storeActiveElement]);

  useEffect(() => {
    const handleKeyDown = (event: KeyboardEvent) => {
      if (event.key === 'Escape') onClose();
    };
    if (!isOpen) return;

    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, [isOpen, onClose]);

  useEffect(() => {
    if (isOpen) {
      requestAnimationFrame(() => {
        restoreFocus();
      });
    }
  }, [isOpen, restoreFocus]);

  if (!isOpen) return null;

  return (
    <div className="modal-backdrop" role="presentation">
      <div
        ref={containerRef}
        className="modal"
        role="dialog"
        aria-modal="true"
        aria-labelledby="accessible-modal-title"
        tabIndex={-1}
      >
        <header>
          <h2 id="accessible-modal-title">{title}</h2>
        </header>
        <div>{children}</div>
        <footer>
          <button type="button" onClick={onClose}>
            Close
          </button>
        </footer>
      </div>
    </div>
  );
}
