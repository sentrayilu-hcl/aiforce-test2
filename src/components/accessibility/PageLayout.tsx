import React from 'react';
import { SkipToContentLink } from './SkipToContentLink';

export interface PageLayoutProps {
  header?: React.ReactNode;
  navigation?: React.ReactNode;
  footer?: React.ReactNode;
  children: React.ReactNode;
  mainId?: string;
}

export function PageLayout({
  header,
  navigation,
  footer,
  children,
  mainId = 'main-content'
}: PageLayoutProps) {
  return (
    <div className="page-layout">
      <SkipToContentLink targetId={mainId} />
      <header>{header}</header>
      {navigation ? <nav aria-label="Primary">{navigation}</nav> : null}
      <main id={mainId} tabIndex={-1}>
        {children}
      </main>
      <footer>{footer}</footer>
    </div>
  );
}
