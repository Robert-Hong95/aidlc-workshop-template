import { describe, it, expect, vi } from 'vitest';
import { render, screen, fireEvent } from '@testing-library/react';
import { SetupForm } from '../../components/features/setup/SetupForm';
import { PasswordConfirmModal } from '../../components/features/setup/PasswordConfirmModal';

describe('SetupForm', () => {
  it('renders all input fields', () => {
    render(<SetupForm onSubmit={() => {}} isLoading={false} />);
    expect(screen.getByTestId('input-store-code')).toBeInTheDocument();
    expect(screen.getByTestId('input-table-no')).toBeInTheDocument();
    expect(screen.getByTestId('input-password')).toBeInTheDocument();
    expect(screen.getByTestId('submit-btn')).toBeInTheDocument();
  });

  it('shows error message when error prop set', () => {
    render(<SetupForm onSubmit={() => {}} isLoading={false} error="인증 실패" />);
    expect(screen.getByText('인증 실패')).toBeInTheDocument();
  });

  it('disables submit when loading', () => {
    render(<SetupForm onSubmit={() => {}} isLoading={true} />);
    expect(screen.getByTestId('submit-btn')).toBeDisabled();
  });
});

describe('PasswordConfirmModal', () => {
  it('renders when isOpen=true', () => {
    render(<PasswordConfirmModal isOpen={true} onConfirm={() => {}} onCancel={() => {}} isLoading={false} />);
    expect(screen.getByTestId('password-confirm-modal')).toBeInTheDocument();
  });

  it('hidden when isOpen=false', () => {
    render(<PasswordConfirmModal isOpen={false} onConfirm={() => {}} onCancel={() => {}} isLoading={false} />);
    expect(screen.queryByTestId('password-confirm-modal')).not.toBeInTheDocument();
  });

  it('calls onCancel when cancel clicked', () => {
    const onCancel = vi.fn();
    render(<PasswordConfirmModal isOpen={true} onConfirm={() => {}} onCancel={onCancel} isLoading={false} />);
    fireEvent.click(screen.getByTestId('modal-cancel'));
    expect(onCancel).toHaveBeenCalled();
  });
});
