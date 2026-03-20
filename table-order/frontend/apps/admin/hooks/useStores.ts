import { useState, useCallback } from 'react';
import type { Store } from '@table-order/api-client';

const API = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

export function useStores() {
  const [stores, setStores] = useState<Store[]>([]);
  const [isLoading, setIsLoading] = useState(false);

  const fetchStores = useCallback(async () => {
    setIsLoading(true);
    try {
      const res = await fetch(`${API}/api/admin/stores`);
      const json = await res.json();
      if (json.success) setStores(json.data || []);
    } finally { setIsLoading(false); }
  }, []);

  const createStore = useCallback(async (data: { name: string; address?: string; phone?: string }) => {
    await fetch(`${API}/api/admin/stores`, {
      method: 'POST', headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ storeCode: data.name.toUpperCase().replace(/\s/g, ''), name: data.name }),
    });
  }, []);

  const updateStore = useCallback(async (id: number, data: { name?: string }) => {
    await fetch(`${API}/api/admin/stores/${id}`, {
      method: 'PUT', headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name: data.name }),
    });
  }, []);

  return { stores, isLoading, fetchStores, createStore, updateStore };
}
