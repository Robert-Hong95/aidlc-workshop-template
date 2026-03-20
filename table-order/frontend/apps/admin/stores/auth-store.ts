import { create } from 'zustand';
import type { AdminTokenResponse } from '@table-order/api-client';

const STORAGE_KEY = 'admin-auth';

interface AdminAuthState {
  accessToken: string | null;
  refreshToken: string | null;
  storeId: number | null;
  storeName: string | null;
  adminId: number | null;
  username: string | null;
  isAuthenticated: boolean;
  login: (response: AdminTokenResponse) => void;
  logout: () => void;
  setTokens: (access: string, refresh: string) => void;
  hydrate: () => void;
}

export const useAdminAuthStore = create<AdminAuthState>()((set) => ({
  accessToken: null,
  refreshToken: null,
  storeId: null,
  storeName: null,
  adminId: null,
  username: null,
  isAuthenticated: false,

  login: (response) => {
    const { accessToken, refreshToken, storeId, storeName, adminId, username } = response;
    localStorage.setItem(STORAGE_KEY, JSON.stringify({ accessToken, refreshToken, storeId, storeName, adminId, username }));
    set({ accessToken, refreshToken, storeId, storeName, adminId, username, isAuthenticated: true });
  },

  logout: () => {
    localStorage.removeItem(STORAGE_KEY);
    set({ accessToken: null, refreshToken: null, storeId: null, storeName: null, adminId: null, username: null, isAuthenticated: false });
  },

  setTokens: (access, refresh) => {
    set({ accessToken: access, refreshToken: refresh });
    const stored = localStorage.getItem(STORAGE_KEY);
    if (stored) {
      const data = JSON.parse(stored);
      localStorage.setItem(STORAGE_KEY, JSON.stringify({ ...data, accessToken: access, refreshToken: refresh }));
    }
  },

  hydrate: () => {
    const stored = localStorage.getItem(STORAGE_KEY);
    if (stored) {
      const data = JSON.parse(stored);
      set({ ...data, isAuthenticated: true });
    }
  },
}));
