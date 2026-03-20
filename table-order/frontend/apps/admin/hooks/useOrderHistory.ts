import { useState, useCallback } from 'react';
import type { OrderHistoryItem, OrderHistoryFilter } from '@table-order/api-client';

const API = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

export function useOrderHistory(tableId: number) {
  const today = new Date().toISOString().split('T')[0];
  const [history, setHistory] = useState<OrderHistoryItem[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [filter, setFilter] = useState<OrderHistoryFilter>({ from: today, to: today });

  const fetchHistory = useCallback(async () => {
    setIsLoading(true);
    try {
      const res = await fetch(`${API}/api/admin/tables/${tableId}/order-history?dateFrom=${filter.from}&dateTo=${filter.to}`);
      const json = await res.json();
      if (json.success) setHistory(json.data || []);
    } finally { setIsLoading(false); }
  }, [tableId, filter]);

  return { history, isLoading, filter, setFilter, fetchHistory };
}
