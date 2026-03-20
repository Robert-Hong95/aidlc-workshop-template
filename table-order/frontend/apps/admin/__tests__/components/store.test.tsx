import { describe, it, expect, vi } from 'vitest';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { StoreForm } from '../../components/features/store/StoreForm';
import { StoreList } from '../../components/features/store/StoreList';
import { StoreSwitcher } from '../../components/features/store/StoreSwitcher';

describe('StoreForm', () => {
  it('TC-6FE-009: renders name field as required', () => {
    render(<StoreForm onSubmit={() => {}} isLoading={false} />);
    expect(screen.getByTestId('input-store-name')).toBeInTheDocument();
  });

  it('TC-6FE-010: shows validation error when name empty on submit', async () => {
    const onSubmit = vi.fn();
    render(<StoreForm onSubmit={onSubmit} isLoading={false} />);
    fireEvent.click(screen.getByTestId('store-submit-btn'));
    await waitFor(() => {
      expect(screen.getByText(/매장명/)).toBeInTheDocument();
    });
    expect(onSubmit).not.toHaveBeenCalled();
  });

  it('TC-6FE-011: prefills data when editing', () => {
    const store = { id: 1, name: 'Test', code: 'S001', address: '서울', createdAt: '' };
    render(<StoreForm onSubmit={() => {}} isLoading={false} store={store} />);
    expect(screen.getByTestId('input-store-name')).toHaveValue('Test');
    expect(screen.getByTestId('input-store-address')).toHaveValue('서울');
  });
});

describe('StoreList', () => {
  const stores = [
    { id: 1, name: 'Store A', code: 'S001', createdAt: '2026-01-01' },
    { id: 2, name: 'Store B', code: 'S002', createdAt: '2026-01-02' },
  ];

  it('TC-6FE-012: renders store list', () => {
    render(<StoreList stores={stores} onEdit={() => {}} />);
    expect(screen.getByText('Store A')).toBeInTheDocument();
    expect(screen.getByText('Store B')).toBeInTheDocument();
  });

  it('TC-6FE-013: calls onEdit when edit button clicked', () => {
    const onEdit = vi.fn();
    render(<StoreList stores={stores} onEdit={onEdit} />);
    fireEvent.click(screen.getAllByTestId('store-edit-btn')[0]);
    expect(onEdit).toHaveBeenCalledWith(stores[0]);
  });
});

describe('StoreSwitcher', () => {
  const stores = [
    { id: 1, name: 'Store A', code: 'S001', createdAt: '' },
    { id: 2, name: 'Store B', code: 'S002', createdAt: '' },
  ];

  it('TC-6FE-014: shows dropdown on click', () => {
    render(<StoreSwitcher stores={stores} currentStoreId={1} onSwitch={() => {}} />);
    fireEvent.click(screen.getByTestId('store-switcher'));
    expect(screen.getByTestId('store-dropdown')).toBeInTheDocument();
  });

  it('TC-6FE-015: calls onSwitch when store selected', () => {
    const onSwitch = vi.fn();
    render(<StoreSwitcher stores={stores} currentStoreId={1} onSwitch={onSwitch} />);
    fireEvent.click(screen.getByTestId('store-switcher'));
    fireEvent.click(screen.getByText('Store B'));
    expect(onSwitch).toHaveBeenCalledWith(2);
  });
});
