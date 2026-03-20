import { useState, useCallback } from 'react';
import type { OrderData } from '../components/features/order/OrderCard';

export function useOrders() {
  const [orders, setOrders] = useState<OrderData[]>([]);
  const [isLoading, setIsLoading] = useState(false);

  const fetchOrders = useCallback(async (_sessionId: number) => {
    setIsLoading(true);
    try {
      // API deferred
    } finally {
      setIsLoading(false);
    }
  }, []);

  const createOrder = useCallback(async (_data: { storeId: number; tableId: number; items: { menuId: number; menuName: string; quantity: number; price: number }[] }): Promise<{ orderNo: string } | null> => {
    setIsLoading(true);
    try {
      // API deferred
      return null;
    } finally {
      setIsLoading(false);
    }
  }, []);

  return { orders, isLoading, fetchOrders, createOrder };
}
