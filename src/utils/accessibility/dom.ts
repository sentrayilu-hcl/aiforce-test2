export function getFocusableElements(container: HTMLElement): HTMLElement[] {
  const selectors = [
    'a[href]',
    'button:not([disabled])',
    'input:not([disabled])',
    'select:not([disabled])',
    'textarea:not([disabled])',
    '[tabindex]:not([tabindex="-1"])',
    '[contenteditable="true"]'
  ].join(',');

  return Array.from(container.querySelectorAll<HTMLElement>(selectors)).filter(
    (el) => !el.hasAttribute('disabled') && !el.getAttribute('aria-hidden')
  );
}

export function focusElementOrFallback(target: HTMLElement | null | undefined): void {
  if (target && typeof target.focus === 'function') {
    target.focus();
  }
}

export function isEscapeKey(event: KeyboardEvent): boolean {
  return event.key === 'Escape';
}
