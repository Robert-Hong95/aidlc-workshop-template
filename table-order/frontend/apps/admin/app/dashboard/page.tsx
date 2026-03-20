'use client';

import { useState, useEffect } from 'react';
import { Spinner, ConfirmDialog } from '@table-order/ui';
import { TableCardGrid } from '../../components/features/dashboard/TableCardGrid';
import { OrderDetailModal } from '../../components/features/dashboard/OrderDetailModal';
import { useDashboard } from '../../hooks/useDashboard';
import { useAdminAuthStore } from '../../stores/auth-store';
import type { DashboardOrder } from '../../components/features/dashboard/OrderPreview';

export default function DashboardPage() {
  const { tables, isLoading, fetchDashboard, changeOrderStatus, deleteOrder } = useDashboard();
  const storeId = useAdminAuthStore((s) => s.storeId);
  const storeName = useAdminAuthStore((s) => s.storeName);
  const [selectedOrder, setSelectedOrder] = useState<DashboardOrder | null>(null);
  const [deleteTarget, setDeleteTarget] = useState<number | null>(null);

  useEffect(() => {
    useAdminAuthStore.getState().hydrate();
  }, []);

  useEffect(() => {
    if (storeId) fetchDashboard(storeId);
  }, [storeId, fetchDashboard]);

  const handleTableClick = (tableId: number) => {
    const table = tables.find((t) => t.tableId === tableId);
    if (table?.orders.length) setSelectedOrder(table.orders[0]);
  };

  const handleDelete = async () => {
    if (deleteTarget !== null) {
      await deleteOrder(deleteTarget);
      setDeleteTarget(null);
      setSelectedOrder(null);
      if (storeId) fetchDashboard(storeId);
    }
  };

  const handleStatusChange = async (orderId: number, status: string) => {
    await changeOrderStatus(orderId, status);
    if (storeId) fetchDashboard(storeId);
  };

  if (isLoading) {
    return <div className="flex items-center justify-center min-h-[400px]"><Spinner size="lg" /></div>;
  }

  return (
    <div data-testid="dashboard-page">
      <div className="flex justify-between items-center mb-4">
        <h1 className="text-xl font-bold text-[#232F3E]">주문 대시보드</h1>
        <span className="text-sm text-gray-500">{storeName}</span>
      </div>
      <TableCardGrid
        tables={tables}
        onChangeStatus={handleStatusChange}
        onTableClick={handleTableClick}
      />
      <OrderDetailModal
        isOpen={!!selectedOrder}
        order={selectedOrder}
        onClose={() => setSelectedOrder(null)}
        onChangeStatus={handleStatusChange}
        onDelete={(id) => setDeleteTarget(id)}
      />
      <ConfirmDialog
        isOpen={deleteTarget !== null}
        title="주문 삭제"
        message="이 주문을 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다."
        onConfirm={handleDelete}
        onCancel={() => setDeleteTarget(null)}
      />
    </div>
  );
}
