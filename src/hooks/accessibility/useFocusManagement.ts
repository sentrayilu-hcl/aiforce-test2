import { useCallback, useEffect, useRef } from 'react';
import { getFocusableElements, focusElementOrFallback } from '../../utils/accessibility/dom';

export interface FocusManagementOptions {
  shouldTrapFocus?: boolean;
  restoreFocusOnUnmount?: boolean;
}

export function useFocusManagement<T extends HTMLElement>(
  options: FocusManagementOptions = {}
) {
  const containerRef = useRef<T | null>(null);
  const lastFocusedElementRef = useRef<HTMLElement | null>(null);

  const setContainerRef = useCallback((node: T | null) => {
    containerRef.current = node;
  }, []);

  const storeActiveElement = useCallback(() => {
    const active = document.activeElement;
    if (active instanceof HTMLElement) {
      lastFocusedElementRef.current = active;
    }
  }, []);

  const restoreFocus = useCallback(() => {
    focusElementOrFallback(lastFocusedElementRef.current);
  }, []);

  useEffect(() => {
    if (options.shouldTrapFocus !== true) return;

    const container = containerRef.current;
    if (!container) return;

    const handleKeyDown = (event: KeyboardEvent) => {
      if (event.key !== 'Tab') return;

      const focusable = getFocusableElements(container);
      if (focusable.length === 0) {
        event.preventDefault();
        container.focus();
        return;
      }

      const first = focusable[0];
      const last = focusable[focusable.length - 1];
      const current = document.activeElement;

      if (event.shiftKey && current === first) {
        event.preventDefault();
        last.focus();
      } else if (!event.shiftKey && current === last) {
        event.preventDefault();
        first.focus();
      }
    };

    container.addEventListener('keydown', handleKeyDown);
    return () => container.removeEventListener('keydown', handleKeyDown);
  }, [options.shouldTrapFocus]);

  useEffect(() => {
    return () => {
      if (options.restoreFocusOnUnmount) {
        restoreFocus();
      }
    };
  }, [options.restoreFocusOnUnmount, restoreFocus]);

  return {
    containerRef: setContainerRef,
    storeActiveElement,
    restoreFocus
  };
}
