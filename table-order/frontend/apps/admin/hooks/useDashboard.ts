import { useState, useCallback } from 'react';
import { useAdminAuthStore } from '../stores/auth-store';
import { orderApi } from '@table-order/api-client';
import type { TableCardData } from '../components/features/dashboard/TableCard';

export function useDashboard() {
  const [tables, setTables] = useState<TableCardData[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const token = useAdminAuthStore((s) => s.accessToken);

  const fetchDashboard = useCallback(async (storeId: number) => {
    if (!token) return;
    setIsLoading(true);
    try {
      const orders = await orderApi.activeOrders(token, storeId);
      const tableMap = new Map<number, TableCardData>();
      for (const o of orders) {
        if (!tableMap.has(o.tableId)) {
          tableMap.set(o.tableId, { tableId: o.tableId, tableNo: o.tableId, totalAmount: 0, orders: [] });
        }
        const t = tableMap.get(o.tableId)!;
        t.totalAmount += o.totalAmount;
        t.orders.push({
          id: o.id, status: o.status, totalAmount: o.totalAmount, createdAt: o.createdAt,
          items: o.items.map((i) => ({ menuName: i.menuName, quantity: i.quantity, price: i.unitPrice })),
        });
      }
      setTables(Array.from(tableMap.values()));
    } finally {
      setIsLoading(false);
    }
  }, [token]);

  const changeOrderStatus = useCallback(async (orderId: number, status: string) => {
    if (!token) return;
    await orderApi.updateStatus(token, orderId, status);
  }, [token]);

  const deleteOrder = useCallback(async (orderId: number) => {
    if (!token) return;
    await orderApi.deleteByAdmin(token, orderId);
  }, [token]);

  return { tables, isLoading, fetchDashboard, changeOrderStatus, deleteOrder };
}
