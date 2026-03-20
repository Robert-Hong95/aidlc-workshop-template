import { useState, useCallback } from 'react';
import type { TableCardData } from '../components/features/dashboard/TableCard';
import { useAdminAuthStore } from '../stores/auth-store';

const API = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

function authHeaders() {
  const token = useAdminAuthStore.getState().accessToken;
  return { 'Content-Type': 'application/json', ...(token ? { Authorization: `Bearer ${token}` } : {}) };
}

export function useDashboard() {
  const [tables, setTables] = useState<TableCardData[]>([]);
  const [isLoading, setIsLoading] = useState(false);

  const fetchDashboard = useCallback(async (storeId: number) => {
    setIsLoading(true);
    try {
      const [tablesRes, ordersRes] = await Promise.all([
        fetch(`${API}/api/admin/stores/${storeId}/tables`, { headers: authHeaders() }),
        fetch(`${API}/api/admin/stores/${storeId}/orders`, { headers: authHeaders() }),
      ]);
      const tablesJson = await tablesRes.json();
      const ordersJson = await ordersRes.json();
      const tableList = tablesJson.success ? tablesJson.data : [];
      const orderList = ordersJson.success ? ordersJson.data : [];

      const mapped: TableCardData[] = tableList.map((t: any) => {
        const tableOrders = orderList.filter((o: any) => o.tableId === t.id);
        return {
          tableId: t.id,
          tableNo: t.tableNo,
          totalAmount: tableOrders.reduce((s: number, o: any) => s + o.totalAmount, 0),
          orders: tableOrders.map((o: any) => ({
            id: o.id, orderNo: String(o.id), status: o.status,
            totalAmount: o.totalAmount, createdAt: o.createdAt,
            items: (o.items || []).map((i: any) => ({ menuName: i.menuName, quantity: i.quantity })),
          })),
        };
      });
      setTables(mapped);
    } finally { setIsLoading(false); }
  }, []);

  const changeOrderStatus = useCallback(async (orderId: number, status: string) => {
    await fetch(`${API}/api/admin/orders/${orderId}/status`, {
      method: 'PUT', headers: authHeaders(), body: JSON.stringify({ status }),
    });
  }, []);

  const deleteOrder = useCallback(async (orderId: number) => {
    await fetch(`${API}/api/admin/orders/${orderId}`, { method: 'DELETE', headers: authHeaders() });
  }, []);

  return { tables, isLoading, fetchDashboard, changeOrderStatus, deleteOrder };
}
