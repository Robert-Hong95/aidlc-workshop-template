import { useState, useCallback } from 'react';
import { useAdminAuthStore } from '../stores/auth-store';
import { tableApi, type OrderHistoryItem, type OrderHistoryFilter } from '@table-order/api-client';

export function useOrderHistory(tableId: number) {
  const today = new Date().toISOString().split('T')[0];
  const [history, setHistory] = useState<OrderHistoryItem[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [filter, setFilter] = useState<OrderHistoryFilter>({ from: today, to: today });
  const token = useAdminAuthStore((s) => s.accessToken);

  const fetchHistory = useCallback(async () => {
    if (!token || !tableId) return;
    setIsLoading(true);
    try { setHistory(await tableApi.orderHistory(token, tableId, filter.from, filter.to)); } finally { setIsLoading(false); }
  }, [token, tableId, filter]);

  return { history, isLoading, filter, setFilter, fetchHistory };
}
