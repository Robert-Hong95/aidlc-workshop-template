import { useState, useCallback } from 'react';
import { useAdminAuthStore } from '../stores/auth-store';
import { tableApi, type StoreTable } from '@table-order/api-client';

export function useTables(storeId: number) {
  const [tables, setTables] = useState<StoreTable[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const token = useAdminAuthStore((s) => s.accessToken);

  const fetchTables = useCallback(async () => {
    if (!token || !storeId) return;
    setIsLoading(true);
    try { setTables(await tableApi.list(token, storeId)); } finally { setIsLoading(false); }
  }, [token, storeId]);

  const createTable = useCallback(async (data: { storeId: number; tableNo: number; password: string }) => {
    if (!token) return;
    await tableApi.setup(token, data.storeId, { tableNo: data.tableNo, password: data.password });
    await fetchTables();
  }, [token, fetchTables]);

  const completeSession = useCallback(async (tableId: number) => {
    if (!token) return;
    await tableApi.endSession(token, tableId);
    await fetchTables();
  }, [token, fetchTables]);

  const deleteOrder = useCallback(async (_orderId: number) => {
    // handled via order API
  }, []);

  return { tables, isLoading, fetchTables, createTable, completeSession, deleteOrder };
}
