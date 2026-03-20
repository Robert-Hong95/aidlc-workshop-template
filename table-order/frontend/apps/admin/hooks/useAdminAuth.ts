import { useState, useCallback } from 'react';
import { useAdminAuthStore } from '../stores/auth-store';
import { useLoginLockout } from './useLoginLockout';
import { authApi } from '@table-order/api-client';

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
      const res = await authApi.adminLogin(storeCode, username, password);
      store.login({
        accessToken: res.token, refreshToken: res.token,
        storeId: res.storeId, storeName: res.storeName,
        adminId: 0, username: res.username,
      });
      lockout.resetAttempts();
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
