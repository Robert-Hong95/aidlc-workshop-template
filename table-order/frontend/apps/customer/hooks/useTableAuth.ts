import { useState, useCallback } from 'react';
import { tableLogin } from '@table-order/api-client';
import { useTableAuthStore } from '../stores/auth-store';
import { apiClient } from '../lib/api';

export function useTableAuth() {
  const store = useTableAuthStore();
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const login = useCallback(async (storeCode: string, tableNo: number, password: string) => {
    setIsLoading(true);
    setError(null);
    try {
      const res = await tableLogin(apiClient, { storeCode, tableNo, password });
      store.login(res, storeCode);
    } catch (e: any) {
      setError(e.message || '로그인에 실패했습니다');
    } finally {
      setIsLoading(false);
    }
  }, [store]);

  const autoLogin = useCallback(() => {
    store.hydrate();
    return store.isAuthenticated;
  }, [store]);

  const logout = useCallback(() => { store.logout(); }, [store]);

  return { login, logout, autoLogin, isAuthenticated: store.isAuthenticated, isLoading, error };
}
