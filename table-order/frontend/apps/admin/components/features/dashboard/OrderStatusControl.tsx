'use client';

import { Badge } from '@table-order/ui';

const STATUS_MAP: Record<string, { label: string; color: string }> = {
  PENDING: { label: '대기중', color: 'yellow' },
  PREPARING: { label: '준비중', color: 'blue' },
  COMPLETED: { label: '완료', color: 'green' },
};

export function OrderStatusControl({ status, onChangeStatus }: {
  status: string;
  onChangeStatus: (newStatus: string) => void;
}) {
  const nextStatus: Record<string, string> = { PENDING: 'PREPARING', PREPARING: 'COMPLETED' };
  const next = nextStatus[status];
  const info = STATUS_MAP[status] ?? { label: status, color: 'gray' };

  return (
    <div className="flex items-center gap-2" data-testid="order-status-control">
      <Badge color={info.color}>{info.label}</Badge>
      {next && (
        <button
          onClick={() => onChangeStatus(next)}
          className="text-xs text-blue-600 underline min-h-[44px] px-2"
          data-testid="status-change-btn"
        >
          → {STATUS_MAP[next]?.label}
        </button>
      )}
    </div>
  );
}
