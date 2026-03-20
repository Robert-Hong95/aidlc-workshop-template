import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useEffect } from 'react';
import { getActiveOrders, updateOrderStatus, deleteOrder, createSseConnection } from '@table-order/api-client';
import { apiClient, API_BASE } from '../lib/api';
import { useAdminAuthStore } from '../stores/auth-store';
import type { Order } from '@table-order/api-client';

export function useDashboard() {
  const qc = useQueryClient();
  const storeId = useAdminAuthStore((s) => s.storeId);

  const { data: orders = [], isLoading } = useQuery({
    queryKey: ['activeOrders', storeId],
    queryFn: () => getActiveOrders(apiClient, storeId!),
    enabled: !!storeId,
    refetchInterval: 30000,
  });

  // SSE for real-time updates
  useEffect(() => {
    if (!storeId) return;
    const disconnect = createSseConnection(API_BASE, `/api/admin/stores/${storeId}/sse`, {
      onMessage: () => { qc.invalidateQueries({ queryKey: ['activeOrders', storeId] }); },
    });
    return disconnect;
  }, [storeId, qc]);

  const changeStatusMut = useMutation({
    mutationFn: ({ orderId, status }: { orderId: number; status: string }) => updateOrderStatus(apiClient, orderId, { status }),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['activeOrders', storeId] }),
  });

  const deleteOrderMut = useMutation({
    mutationFn: (orderId: number) => deleteOrder(apiClient, orderId),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['activeOrders', storeId] }),
  });

  // Group orders by table
  const tableMap = new Map<number, Order[]>();
  orders.forEach((o) => {
    const list = tableMap.get(o.tableId) || [];
    list.push(o);
    tableMap.set(o.tableId, list);
  });

  const tables = Array.from(tableMap.entries()).map(([tableId, tableOrders]) => ({
    tableId,
    tableNo: tableId, // Will be enriched when tables data is available
    totalAmount: tableOrders.reduce((s, o) => s + o.totalAmount, 0),
    orders: tableOrders.map((o) => ({
      id: o.id,
      orderNo: String(o.id),
      status: o.status,
      totalAmount: o.totalAmount,
      createdAt: o.createdAt,
      items: o.items.map((i) => ({ menuName: i.menuName, quantity: i.quantity })),
    })),
  }));

  return {
    tables, isLoading,
    changeOrderStatus: (orderId: number, status: string) => changeStatusMut.mutateAsync({ orderId, status }),
    deleteOrder: deleteOrderMut.mutateAsync,
  };
}
