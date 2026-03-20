import { useState, useCallback } from 'react';
import type { StoreTable } from '@table-order/api-client';

const API = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

export function useTables(storeId: number) {
  const [tables, setTables] = useState<StoreTable[]>([]);
  const [isLoading, setIsLoading] = useState(false);

  const fetchTables = useCallback(async () => {
    setIsLoading(true);
    try {
      const res = await fetch(`${API}/api/admin/stores/${storeId}/tables`);
      const json = await res.json();
      if (json.success) setTables(json.data || []);
    } finally { setIsLoading(false); }
  }, [storeId]);

  const createTable = useCallback(async (data: { storeId: number; tableNo: number; password: string }) => {
    await fetch(`${API}/api/admin/stores/${data.storeId}/tables`, {
      method: 'POST', headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ tableNo: data.tableNo, password: data.password }),
    });
  }, []);

  const completeSession = useCallback(async (tableId: number) => {
    await fetch(`${API}/api/admin/tables/${tableId}/end-session`, { method: 'POST' });
  }, []);

  const deleteOrder = useCallback(async (orderId: number) => {
    await fetch(`${API}/api/admin/orders/${orderId}`, { method: 'DELETE' });
  }, []);

  return { tables, isLoading, fetchTables, createTable, completeSession, deleteOrder };
}
