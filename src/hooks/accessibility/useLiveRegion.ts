import { useCallback, useEffect, useRef } from 'react';
import { ACCESSIBILITY } from '../../utils/accessibility/accessibilityConstants';

export function useLiveRegion() {
  const regionRef = useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    if (regionRef.current) return;

    const region = document.createElement('div');
    region.id = ACCESSIBILITY.liveRegionId;
    region.setAttribute('aria-live', 'polite');
    region.setAttribute('aria-atomic', 'true');
    region.style.position = 'absolute';
    region.style.width = '1px';
    region.style.height = '1px';
    region.style.overflow = 'hidden';
    region.style.clip = 'rect(1px, 1px, 1px, 1px)';
    region.style.whiteSpace = 'nowrap';
    document.body.appendChild(region);
    regionRef.current = region;

    return () => {
      region.remove();
      regionRef.current = null;
    };
  }, []);

  const announce = useCallback((message: string, politeness: 'polite' | 'assertive' = 'polite') => {
    if (!regionRef.current) return;
    regionRef.current.setAttribute('aria-live', politeness);
    regionRef.current.textContent = '';
    window.setTimeout(() => {
      if (regionRef.current) {
        regionRef.current.textContent = message;
      }
    }, 50);
  }, []);

  return { announce };
}
