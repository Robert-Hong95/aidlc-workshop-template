import { useState, useCallback } from 'react';
import { useTableAuthStore } from '../stores/auth-store';

const API = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

export function useTableAuth() {
  const store = useTableAuthStore();
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const login = useCallback(async (storeCode: string, tableNo: number, password: string) => {
    setIsLoading(true);
    setError(null);
    try {
      const res = await fetch(`${API}/api/customer/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ storeCode, tableNo, password }),
      });
      const json = await res.json();
      if (!json.success) throw new Error(json.error || '로그인에 실패했습니다');
      store.login(json.data, storeCode);
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

  const verifyPassword = useCallback(async (_password: string) => false, []);
  const logout = useCallback(() => { store.logout(); }, [store]);

  return { login, logout, verifyPassword, autoLogin, isAuthenticated: store.isAuthenticated, isLoading, error };
}
