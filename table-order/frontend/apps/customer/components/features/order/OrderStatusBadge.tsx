'use client';

import { Badge } from '@table-order/ui';

const STATUS_MAP: Record<string, { label: string; color: string }> = {
  PENDING: { label: '대기중', color: 'yellow' },
  PREPARING: { label: '준비중', color: 'blue' },
  COMPLETED: { label: '완료', color: 'green' },
};

export function OrderStatusBadge({ status }: { status: string }) {
  const info = STATUS_MAP[status] ?? { label: status, color: 'gray' };
  return <Badge color={info.color}>{info.label}</Badge>;
}
