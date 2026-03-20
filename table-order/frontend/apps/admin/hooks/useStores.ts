import { useState, useCallback } from 'react';
import { useAdminAuthStore } from '../stores/auth-store';
import { storeApi, type Store } from '@table-order/api-client';

export function useStores() {
  const [stores, setStores] = useState<Store[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const token = useAdminAuthStore((s) => s.accessToken);

  const fetchStores = useCallback(async () => {
    if (!token) return;
    setIsLoading(true);
    try { setStores(await storeApi.list(token)); } finally { setIsLoading(false); }
  }, [token]);

  const createStore = useCallback(async (data: { name: string }) => {
    if (!token) return;
    await storeApi.create(token, data);
    await fetchStores();
  }, [token, fetchStores]);

  const updateStore = useCallback(async (id: number, data: { name?: string }) => {
    if (!token) return;
    await storeApi.update(token, id, data);
    await fetchStores();
  }, [token, fetchStores]);

  return { stores, isLoading, fetchStores, createStore, updateStore };
}
