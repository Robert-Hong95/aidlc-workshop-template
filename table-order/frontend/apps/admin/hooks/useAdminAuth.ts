import { useState, useCallback } from 'react';
import { useAdminAuthStore } from '../stores/auth-store';
import { useLoginLockout } from './useLoginLockout';

const API = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

export function useAdminAuth() {
  const store = useAdminAuthStore();
  const lockout = useLoginLockout();
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const login = useCallback(async (storeCode: string, username: string, password: string) => {
    if (lockout.isLocked) {
      setError(`${lockout.remainingSeconds}초 후 다시 시도해주세요`);
      return;
    }
    setIsLoading(true);
    setError(null);
    try {
      const res = await fetch(`${API}/api/admin/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ storeCode, username, password }),
      });
      const json = await res.json();
      if (!json.success) throw new Error(json.error || '로그인에 실패했습니다');
      lockout.resetAttempts();
      store.login(json.data);
    } catch (e: any) {
      lockout.recordFailure();
      setError(e.message || '로그인에 실패했습니다');
    } finally {
      setIsLoading(false);
    }
  }, [store, lockout]);

  const logout = useCallback(() => { store.logout(); }, [store]);

  return {
    login, logout,
    isAuthenticated: store.isAuthenticated,
    isLoading, error,
    loginAttempts: lockout.attempts,
    lockoutRemaining: lockout.remainingSeconds,
  };
}
