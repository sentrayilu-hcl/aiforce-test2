import React from 'react';
import { fireEvent, render, screen } from '@testing-library/react';
import {
  AccessibleAlert,
  AccessibleFormField,
  AccessibleModal,
  AccessibleToast,
  PageLayout,
  SkipToContentLink
} from '../../src/components/accessibility';
import { useFocusManagement } from '../../src/hooks/accessibility/useFocusManagement';
import { useKeyboardNavigation } from '../../src/hooks/accessibility/useKeyboardNavigation';
import { useLiveRegion } from '../../src/hooks/accessibility/useLiveRegion';

describe('SCRUM-179 accessibility unit tests', () => {
  test('TC-SCRUM-179-001 skip link is present and targets main content', () => {
    render(<SkipToContentLink />);
    expect(screen.getByRole('link', { name: 'Skip to main content' })).toBeInTheDocument();
    expect(screen.getByRole('link', { name: 'Skip to main content' })).toHaveAttribute('href', '#main-content');
  });

  test('TC-SCRUM-179-002 page layout exposes landmarks and main region', () => {
    render(<PageLayout header={<div>Header</div>} navigation={<div>Nav</div>} footer={<div>Footer</div>}><h1>Homepage</h1></PageLayout>);
    expect(screen.getByRole('banner')).toBeInTheDocument();
    expect(screen.getByRole('navigation', { name: 'Primary' })).toBeInTheDocument();
    expect(screen.getByRole('main')).toBeInTheDocument();
    expect(screen.getByRole('contentinfo')).toBeInTheDocument();
  });

  test('TC-SCRUM-179-007 alert announces validation errors', () => {
    render(<AccessibleAlert tone="error" title="Validation Error">Email is required</AccessibleAlert>);
    expect(screen.getByRole('alert')).toBeInTheDocument();
    expect(screen.getByText('Email is required')).toBeVisible();
  });

  test('TC-SCRUM-179-008 toast renders as polite status and dismisses', () => {
    const onDismiss = jest.fn();
    render(<AccessibleToast message="Item added to wishlist" tone="success" onDismiss={onDismiss} />);
    expect(screen.getByRole('status')).toHaveTextContent('Item added to wishlist');
    fireEvent.click(screen.getByRole('button', { name: 'Dismiss notification' }));
    expect(onDismiss).toHaveBeenCalledTimes(1);
  });

  test('TC-SCRUM-179-006 form field binds label, hint, and error', () => {
    render(<AccessibleFormField id="email" label="Email address" hint="Used for order updates" error="Email is required" placeholder="name@example.com" />);
    const input = screen.getByLabelText('Email address');
    expect(input).toHaveAttribute('aria-invalid', 'true');
    expect(input).toHaveAttribute('aria-describedby', expect.stringContaining('email-hint'));
    expect(input).toHaveAttribute('aria-describedby', expect.stringContaining('email-error'));
    expect(screen.getByRole('alert')).toHaveTextContent('Email is required');
  });

  test('TC-SCRUM-179-004 modal renders with dialog semantics', () => {
    render(<AccessibleModal isOpen title="Test Modal" onClose={jest.fn()}><p>Modal content</p></AccessibleModal>);
    expect(screen.getByRole('dialog')).toBeInTheDocument();
    expect(screen.getByRole('heading', { name: 'Test Modal' })).toBeInTheDocument();
  });

  test('TC-SCRUM-179-005 escape key closes modal', () => {
    const onClose = jest.fn();
    render(<AccessibleModal isOpen title="Test Modal" onClose={onClose}><p>Modal content</p></AccessibleModal>);
    fireEvent.keyDown(window, { key: 'Escape' });
    expect(onClose).toHaveBeenCalled();
  });

  test('useKeyboardNavigation maps keyboard actions', () => {
    function Harness() {
      const onKeyDown = useKeyboardNavigation({
        Enter: () => document.body.setAttribute('data-enter', 'true'),
        Escape: () => document.body.setAttribute('data-escape', 'true')
      });
      return <div tabIndex={0} onKeyDown={onKeyDown}>Harness</div>;
    }
    render(<Harness />);
    const node = screen.getByText('Harness');
    fireEvent.keyDown(node, { key: 'Enter' });
    expect(document.body).toHaveAttribute('data-enter', 'true');
    fireEvent.keyDown(node, { key: 'Escape' });
    expect(document.body).toHaveAttribute('data-escape', 'true');
  });

  test('useLiveRegion announces a message', async () => {
    function Harness() {
      const { announce } = useLiveRegion();
      return <button type="button" onClick={() => announce('Saved successfully')}>Announce</button>;
    }
    render(<Harness />);
    fireEvent.click(screen.getByRole('button', { name: 'Announce' }));
    expect(await screen.findByText('Saved successfully')).toBeInTheDocument();
  });

  test('useFocusManagement restores focus to trigger element', () => {
    function Harness() {
      const { containerRef, storeActiveElement, restoreFocus } = useFocusManagement<HTMLDivElement>({ shouldTrapFocus: false, restoreFocusOnUnmount: false });
      return <><button type="button" data-testid="trigger" onClick={storeActiveElement}>Trigger</button><div ref={containerRef} tabIndex={-1}><button type="button" onClick={restoreFocus}>Restore</button></div></>;
    }
    render(<Harness />);
    const trigger = screen.getByTestId('trigger') as HTMLButtonElement;
    trigger.focus();
    expect(trigger).toHaveFocus();
    fireEvent.click(trigger);
    fireEvent.click(screen.getByRole('button', { name: 'Restore' }));
    expect(trigger).toHaveFocus();
  });
});
