import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { PageLayout, AccessibleFormField, AccessibleModal, AccessibleAlert } from '../../src/components/accessibility';

describe('accessibility components', () => {
  test('renders skip link and main landmark', () => {
    render(
      <PageLayout>
        <h1>Main Content</h1>
      </PageLayout>
    );

    expect(screen.getByText('Skip to main content')).toBeInTheDocument();
    expect(screen.getByRole('main')).toBeInTheDocument();
  });

  test('form field associates label and error', () => {
    render(
      <AccessibleFormField
        id="email"
        label="Email address"
        error="Email is required"
      />
    );

    expect(screen.getByLabelText('Email address')).toBeInTheDocument();
    expect(screen.getByText('Email is required')).toHaveAttribute('role', 'alert');
  });

  test('modal renders dialog semantics', () => {
    render(
      <AccessibleModal isOpen title="Test Modal" onClose={jest.fn()}>
        <p>Modal content</p>
      </AccessibleModal>
    );

    expect(screen.getByRole('dialog')).toBeInTheDocument();
    expect(screen.getByText('Test Modal')).toBeInTheDocument();
  });

  test('alert uses alert role for errors', () => {
    render(
      <AccessibleAlert tone="error" title="Error">
        Something went wrong
      </AccessibleAlert>
    );

    expect(screen.getByRole('alert')).toBeInTheDocument();
  });

  test('escape closes modal', () => {
    const onClose = jest.fn();
    render(
      <AccessibleModal isOpen title="Test Modal" onClose={onClose}>
        <p>Modal content</p>
      </AccessibleModal>
    );

    fireEvent.keyDown(window, { key: 'Escape' });
    expect(onClose).toHaveBeenCalled();
  });
});
