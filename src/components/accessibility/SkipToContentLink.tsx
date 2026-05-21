import React from 'react';
import { ACCESSIBILITY } from '../../utils/accessibility/accessibilityConstants';

export interface SkipToContentLinkProps {
  targetId?: string;
  label?: string;
}

export function SkipToContentLink({
  targetId = ACCESSIBILITY.skipLinkTargetId,
  label = 'Skip to main content'
}: SkipToContentLinkProps) {
  return (
    <a className="skip-link" href={`#${targetId}`}>
      {label}
    </a>
  );
}
