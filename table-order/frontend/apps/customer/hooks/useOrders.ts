import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useEffect } from 'react';
import { createOrder, getOrdersByTable, createSseConnection } from '@table-order/api-client';
import { apiClient, API_BASE } from '../lib/api';
import { useTableAuthStore } from '../stores/auth-store';
import type { OrderItemRequest } from '@table-order/api-client';

export function useOrders() {
  const qc = useQueryClient();
  const storeId = useTableAuthStore((s) => s.storeId);
  const tableId = useTableAuthStore((s) => s.tableId);

  const { data: orders = [], isLoading } = useQuery({
    queryKey: ['orders', storeId, tableId],
    queryFn: () => getOrdersByTable(apiClient, storeId!, tableId!),
    enabled: !!storeId && !!tableId,
  });

  // SSE for real-time order status updates
  useEffect(() => {
    if (!storeId || !tableId) return;
    const disconnect = createSseConnection(API_BASE, `/api/customer/stores/${storeId}/tables/${tableId}/sse`, {
      onMessage: () => { qc.invalidateQueries({ queryKey: ['orders', storeId, tableId] }); },
    });
    return disconnect;
  }, [storeId, tableId, qc]);

  const createMut = useMutation({
    mutationFn: (items: OrderItemRequest[]) => createOrder(apiClient, storeId!, tableId!, { items }),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['orders', storeId, tableId] }),
  });

  return { orders, isLoading, createOrder: createMut.mutateAsync };
}
