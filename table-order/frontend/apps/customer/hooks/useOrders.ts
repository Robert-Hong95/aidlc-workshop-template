import { useState, useCallback } from 'react';
import type { OrderData } from '../components/features/order/OrderCard';

const API = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

export function useOrders() {
  const [orders, setOrders] = useState<OrderData[]>([]);
  const [isLoading, setIsLoading] = useState(false);

  const fetchOrders = useCallback(async (storeId: number, tableId: number) => {
    setIsLoading(true);
    try {
      const res = await fetch(`${API}/api/customer/stores/${storeId}/tables/${tableId}/orders`);
      const json = await res.json();
      if (json.success) setOrders(json.data || []);
    } finally {
      setIsLoading(false);
    }
  }, []);

  const createOrder = useCallback(async (data: { storeId: number; tableId: number; items: { menuId: number; menuName: string; quantity: number; price: number }[] }): Promise<{ orderNo: string } | null> => {
    setIsLoading(true);
    try {
      const res = await fetch(`${API}/api/customer/stores/${data.storeId}/tables/${data.tableId}/orders`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ items: data.items.map(i => ({ menuId: i.menuId, quantity: i.quantity })) }),
      });
      const json = await res.json();
      if (json.success) return { orderNo: String(json.data.id) };
      return null;
    } finally {
      setIsLoading(false);
    }
  }, []);

  return { orders, isLoading, fetchOrders, createOrder };
}
