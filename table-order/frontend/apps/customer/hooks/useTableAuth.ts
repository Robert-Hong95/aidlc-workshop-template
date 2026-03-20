import { useState, useCallback } from 'react';
import { useTableAuthStore } from '../stores/auth-store';

export function useTableAuth() {
  const store = useTableAuthStore();
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const login = useCallback(async (storeCode: string, tableNo: number, password: string) => {
    setIsLoading(true);
    setError(null);
    try {
      // API integration deferred
      throw new Error('API not connected');
    } catch (e: any) {
      setError(e.message || '로그인에 실패했습니다');
    } finally {
      setIsLoading(false);
    }
  }, [store]);

  const autoLogin = useCallback(async () => {
    store.hydrate();
    if (!store.refreshToken) return false;
    try {
      // Token refresh deferred
      return store.isAuthenticated;
    } catch {
      store.logout();
      return false;
    }
  }, [store]);

  const verifyPassword = useCallback(async (password: string) => {
    setIsLoading(true);
    try {
      // API integration deferred
      throw new Error('API not connected');
    } catch (e: any) {
      setError(e.message);
      return false;
    } finally {
      setIsLoading(false);
    }
  }, []);

  const logout = useCallback(() => { store.logout(); }, [store]);

  return { login, logout, verifyPassword, autoLogin, isAuthenticated: store.isAuthenticated, isLoading, error };
}
