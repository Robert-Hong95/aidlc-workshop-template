import { useState, useCallback } from 'react';
import { useTableAuthStore } from '../stores/auth-store';
import { authApi } from '@table-order/api-client';

export function useTableAuth() {
  const store = useTableAuthStore();
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const login = useCallback(async (storeCode: string, tableNo: number, password: string) => {
    setIsLoading(true);
    setError(null);
    try {
      const res = await authApi.tableLogin(storeCode, tableNo, password);
      store.login({
        accessToken: res.token, refreshToken: res.token,
        storeId: res.storeId, storeName: res.storeName,
        tableId: res.tableId, tableNo: res.tableNo,
      }, storeCode);
    } catch (e: any) {
      setError(e.message || '로그인에 실패했습니다');
    } finally {
      setIsLoading(false);
    }
  }, [store]);

  const autoLogin = useCallback(async () => {
    store.hydrate();
    return store.isAuthenticated;
  }, [store]);

  const verifyPassword = useCallback(async (_password: string) => {
    return false; // deferred
  }, []);

  const logout = useCallback(() => { store.logout(); }, [store]);

  return { login, logout, verifyPassword, autoLogin, isAuthenticated: store.isAuthenticated, isLoading, error };
}
