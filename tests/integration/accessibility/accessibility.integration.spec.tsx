import React, { useState } from 'react';
import { fireEvent, render, screen } from '@testing-library/react';
import {
  AccessibleFormField,
  AccessibleModal,
  AccessibleToast,
  PageLayout,
  SkipToContentLink
} from '../../src/components/accessibility';

function DemoPage() {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [formError, setFormError] = useState('');
  const [toastVisible, setToastVisible] = useState(false);

  return (
    <PageLayout header={<div>Store Header</div>} navigation={<a href="/collections">Collections</a>} footer={<div>Store Footer</div>}>
      <h1>Homepage</h1>
      <SkipToContentLink />
      <button type="button" onClick={() => setIsModalOpen(true)}>Open dialog</button>
      <button type="button" onClick={() => setToastVisible(true)}>Show toast</button>
      <form onSubmit={(e) => { e.preventDefault(); setFormError('Email is required'); }}>
        <AccessibleFormField id="email" label="Email address" error={formError} hint="Used for order updates" />
        <button type="submit">Submit</button>
      </form>
      {toastVisible ? <AccessibleToast message="Added to wishlist" tone="success" onDismiss={() => setToastVisible(false)} /> : null}
      <AccessibleModal isOpen={isModalOpen} title="Product details" onClose={() => setIsModalOpen(false)}><button type="button">Confirm</button></AccessibleModal>
    </PageLayout>
  );
}

describe('SCRUM-179 accessibility integration tests', () => {
  test('TC-SCRUM-179-001 and TC-SCRUM-179-002 skip link and landmarks render together', () => {
    render(<DemoPage />);
    expect(screen.getByRole('link', { name: 'Skip to main content' })).toBeInTheDocument();
    expect(screen.getByRole('main')).toBeInTheDocument();
    expect(screen.getByRole('navigation', { name: 'Primary' })).toBeInTheDocument();
  });

  test('TC-SCRUM-179-003 keyboard users can reach primary controls', () => {
    render(<DemoPage />);
    const openDialogButton = screen.getByRole('button', { name: 'Open dialog' });
    const showToastButton = screen.getByRole('button', { name: 'Show toast' });
    openDialogButton.focus();
    expect(openDialogButton).toHaveFocus();
    fireEvent.keyDown(openDialogButton, { key: 'Tab' });
    showToastButton.focus();
    expect(showToastButton).toHaveFocus();
  });

  test('TC-SCRUM-179-006 form validation renders accessible error', () => {
    render(<DemoPage />);
    fireEvent.click(screen.getByRole('button', { name: 'Submit' }));
    expect(screen.getByRole('alert')).toHaveTextContent('Email is required');
    expect(screen.getByLabelText('Email address')).toHaveAttribute('aria-invalid', 'true');
  });

  test('TC-SCRUM-179-004 and TC-SCRUM-179-005 modal opens and closes with accessible semantics', () => {
    render(<DemoPage />);
    fireEvent.click(screen.getByRole('button', { name: 'Open dialog' }));
    expect(screen.getByRole('dialog')).toBeInTheDocument();
    fireEvent.keyDown(window, { key: 'Escape' });
    expect(screen.queryByRole('dialog')).not.toBeInTheDocument();
  });

  test('TC-SCRUM-179-008 toast is accessible and dismissible', () => {
    render(<DemoPage />);
    fireEvent.click(screen.getByRole('button', { name: 'Show toast' }));
    expect(screen.getByRole('status')).toHaveTextContent('Added to wishlist');
    fireEvent.click(screen.getByRole('button', { name: 'Dismiss notification' }));
    expect(screen.queryByRole('status')).not.toBeInTheDocument();
  });
});
