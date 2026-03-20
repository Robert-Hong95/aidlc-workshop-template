'use client';

import { TableCard, type TableCardData } from './TableCard';
import { EmptyState } from '@table-order/ui';

export function TableCardGrid({ tables, onChangeStatus, onTableClick }: {
  tables: TableCardData[];
  onChangeStatus: (orderId: number, status: string) => void;
  onTableClick: (tableId: number) => void;
}) {
  if (tables.length === 0) return <EmptyState message="등록된 테이블이 없습니다" />;

  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4" data-testid="table-card-grid">
      {tables.map((t) => (
        <TableCard key={t.tableId} data={t} onChangeStatus={onChangeStatus} onClick={() => onTableClick(t.tableId)} />
      ))}
    </div>
  );
}
