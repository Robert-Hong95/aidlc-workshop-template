import { useState, useCallback } from 'react';
import type { TableCardData } from '../components/features/dashboard/TableCard';

export function useDashboard() {
  const [tables, setTables] = useState<TableCardData[]>([]);
  const [isLoading, setIsLoading] = useState(false);

  const fetchDashboard = useCallback(async (_storeId: number) => {
    setIsLoading(true);
    try {
      // API deferred
    } finally {
      setIsLoading(false);
    }
  }, []);

  const changeOrderStatus = useCallback(async (_orderId: number, _status: string) => {
    // API deferred
  }, []);

  const deleteOrder = useCallback(async (_orderId: number) => {
    // API deferred
  }, []);

  return { tables, isLoading, fetchDashboard, changeOrderStatus, deleteOrder };
}
