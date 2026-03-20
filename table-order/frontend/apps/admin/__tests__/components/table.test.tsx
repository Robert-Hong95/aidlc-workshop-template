import { describe, it, expect, vi } from 'vitest';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { ViewToggle } from '../../components/features/table/ViewToggle';
import { TableSetupForm } from '../../components/features/table/TableSetupForm';
import { SessionCompleteDialog } from '../../components/features/table/SessionCompleteDialog';
import { DatePresets } from '../../components/features/table/DatePresets';
import { OrderHistoryModal } from '../../components/features/table/OrderHistoryModal';

describe('ViewToggle', () => {
  it('TC-6FE-019: toggles between list and card mode', () => {
    const onChange = vi.fn();
    render(<ViewToggle mode="list" onChange={onChange} />);
    fireEvent.click(screen.getByTestId('toggle-card'));
    expect(onChange).toHaveBeenCalledWith('card');
  });
});

describe('TableSetupForm', () => {
  it('TC-6FE-016: validates 4-digit PIN', async () => {
    const onSubmit = vi.fn();
    render(<TableSetupForm onSubmit={onSubmit} isLoading={false} />);
    fireEvent.change(screen.getByTestId('input-table-no'), { target: { value: '5' } });
    fireEvent.change(screen.getByTestId('input-table-pin'), { target: { value: '12345' } });
    fireEvent.click(screen.getByTestId('table-submit-btn'));
    await waitFor(() => {
      expect(screen.getByText(/4자리/)).toBeInTheDocument();
    });
    expect(onSubmit).not.toHaveBeenCalled();
  });

  it('TC-6FE-017: validates positive table number', async () => {
    const onSubmit = vi.fn();
    render(<TableSetupForm onSubmit={onSubmit} isLoading={false} />);
    fireEvent.change(screen.getByTestId('input-table-no'), { target: { value: '0' } });
    fireEvent.change(screen.getByTestId('input-table-pin'), { target: { value: '1234' } });
    fireEvent.click(screen.getByTestId('table-submit-btn'));
    await waitFor(() => {
      expect(screen.getByText(/1 이상/)).toBeInTheDocument();
    });
  });

  it('TC-6FE-018: shows error for invalid PIN', async () => {
    render(<TableSetupForm onSubmit={() => {}} isLoading={false} />);
    fireEvent.change(screen.getByTestId('input-table-no'), { target: { value: '1' } });
    fireEvent.change(screen.getByTestId('input-table-pin'), { target: { value: 'abcd' } });
    fireEvent.click(screen.getByTestId('table-submit-btn'));
    await waitFor(() => {
      expect(screen.getByText(/4자리 숫자/)).toBeInTheDocument();
    });
  });
});

describe('SessionCompleteDialog', () => {
  it('TC-6FE-020: shows total amount in dialog', () => {
    render(<SessionCompleteDialog isOpen={true} tableName="테이블 5" totalAmount={45000} onConfirm={() => {}} onCancel={() => {}} />);
    expect(screen.getByText(/45,000/)).toBeInTheDocument();
  });

  it('TC-6FE-021: calls onConfirm when confirmed', () => {
    const onConfirm = vi.fn();
    render(<SessionCompleteDialog isOpen={true} tableName="T5" totalAmount={0} onConfirm={onConfirm} onCancel={() => {}} />);
    fireEvent.click(screen.getByTestId('session-confirm-btn'));
    expect(onConfirm).toHaveBeenCalled();
  });
});

describe('DatePresets', () => {
  it('TC-6FE-024: renders 3 preset buttons', () => {
    render(<DatePresets onSelect={() => {}} />);
    expect(screen.getByText('오늘')).toBeInTheDocument();
    expect(screen.getByText('최근 7일')).toBeInTheDocument();
    expect(screen.getByText('최근 30일')).toBeInTheDocument();
  });

  it('TC-6FE-025: calls onSelect with correct date range', () => {
    const onSelect = vi.fn();
    render(<DatePresets onSelect={onSelect} />);
    fireEvent.click(screen.getByText('오늘'));
    expect(onSelect).toHaveBeenCalled();
    const arg = onSelect.mock.calls[0][0];
    expect(arg.from).toBeDefined();
    expect(arg.to).toBeDefined();
  });
});

describe('OrderHistoryModal', () => {
  it('TC-6FE-022: renders with date presets', () => {
    render(<OrderHistoryModal isOpen={true} tableId={1} onClose={() => {}} history={[]} isLoading={false} filter={{ from: '', to: '' }} onFilterChange={() => {}} />);
    expect(screen.getByText('오늘')).toBeInTheDocument();
  });

  it('TC-6FE-023: hidden when isOpen=false', () => {
    render(<OrderHistoryModal isOpen={false} tableId={1} onClose={() => {}} history={[]} isLoading={false} filter={{ from: '', to: '' }} onFilterChange={() => {}} />);
    expect(screen.queryByTestId('order-history-modal')).not.toBeInTheDocument();
  });
});
