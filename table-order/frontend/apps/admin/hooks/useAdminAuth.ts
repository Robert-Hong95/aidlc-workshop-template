import { useState, useCallback } from 'react';
import { adminLogin } from '@table-order/api-client';
import { useAdminAuthStore } from '../stores/auth-store';
import { apiClient } from '../lib/api';

export function useAdminAuth() {
  const store = useAdminAuthStore();
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const login = useCallback(async (storeCode: string, username: string, password: string) => {
    setIsLoading(true);
    setError(null);
    try {
      const res = await adminLogin(apiClient, { storeCode, username, password });
      store.login(res);
    } catch (e: any) {
      setError(e.message || '로그인에 실패했습니다');
    } finally {
      setIsLoading(false);
    }
  }, [store]);

  const logout = useCallback(() => { store.logout(); }, [store]);

  return { login, logout, isAuthenticated: store.isAuthenticated, isLoading, error, storeId: store.storeId, storeName: store.storeName };
}
