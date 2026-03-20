import { useState, useCallback } from 'react';
import { useAdminAuthStore } from '../stores/auth-store';
import { useLoginLockout } from './useLoginLockout';

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
      // API integration deferred — will use api-client when backend is ready
      throw new Error('API not connected');
    } catch (e: any) {
      lockout.recordFailure();
      setError(e.message || '로그인에 실패했습니다');
    } finally {
      setIsLoading(false);
    }
  }, [store, lockout]);

  const logout = useCallback(() => {
    store.logout();
  }, [store]);

  return {
    login, logout,
    isAuthenticated: store.isAuthenticated,
    isLoading, error,
    loginAttempts: lockout.attempts,
    lockoutRemaining: lockout.remainingSeconds,
  };
}
