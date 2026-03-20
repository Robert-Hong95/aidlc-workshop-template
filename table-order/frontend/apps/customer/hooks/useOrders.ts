import { useState, useCallback } from 'react';
import { useTableAuthStore } from '../stores/auth-store';
import { orderApi } from '@table-order/api-client';
import type { OrderData } from '../components/features/order/OrderCard';

export function useOrders() {
  const [orders, setOrders] = useState<OrderData[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const token = useTableAuthStore((s) => s.accessToken);
  const storeId = useTableAuthStore((s) => s.storeId);
  const tableId = useTableAuthStore((s) => s.tableId);

  const fetchOrders = useCallback(async (_sessionId?: number) => {
    if (!token || !storeId || !tableId) return;
    setIsLoading(true);
    try {
      const data = await orderApi.listByTable(token, storeId, tableId);
      setOrders(data.map((o) => ({
        id: o.id, orderNo: String(o.id), status: o.status, totalAmount: o.totalAmount, createdAt: o.createdAt,
        items: o.items.map((i) => ({ menuName: i.menuName, quantity: i.quantity, price: i.unitPrice })),
      })));
    } finally {
      setIsLoading(false);
    }
  }, [token, storeId, tableId]);

  const createOrder = useCallback(async (data: { storeId: number; tableId: number; items: { menuId: number; menuName: string; quantity: number; price: number }[] }): Promise<{ orderNo: string } | null> => {
    if (!token || !storeId || !tableId) return null;
    setIsLoading(true);
    try {
      const res = await orderApi.create(token, storeId, tableId, {
        items: data.items.map((i) => ({ menuId: i.menuId, quantity: i.quantity })),
      });
      return { orderNo: String(res.id) };
    } catch {
      return null;
    } finally {
      setIsLoading(false);
    }
  }, [token, storeId, tableId]);

  return { orders, isLoading, fetchOrders, createOrder };
}
