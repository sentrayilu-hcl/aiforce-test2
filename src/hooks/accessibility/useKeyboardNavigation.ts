import { useCallback } from 'react';

type KeyboardActionMap = Partial<Record<string, () => void>>;

export function useKeyboardNavigation(actions: KeyboardActionMap) {
  return useCallback(
    (event: React.KeyboardEvent<HTMLElement>) => {
      const action = actions[event.key];
      if (action) {
        event.preventDefault();
        action();
      }
    },
    [actions]
  );
}
