import { useState, useCallback } from 'react';
import type { StoreTable } from '@table-order/api-client';

export function useTables(_storeId: number) {
  const [tables] = useState<StoreTable[]>([]);
  const [isLoading] = useState(false);

  const createTable = useCallback(async (_data: { storeId: number; tableNo: number; password: string }) => {}, []);
  const completeSession = useCallback(async (_tableId: number) => {}, []);
  const deleteOrder = useCallback(async (_orderId: number) => {}, []);

  return { tables, isLoading, createTable, completeSession, deleteOrder };
}
