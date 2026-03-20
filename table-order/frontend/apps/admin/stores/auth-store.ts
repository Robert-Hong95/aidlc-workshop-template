import { create } from 'zustand';
import type { AdminLoginResponse } from '@table-order/api-client';

const STORAGE_KEY = 'admin-auth';

interface AdminAuthState {
  token: string | null;
  storeId: number | null;
  storeName: string | null;
  username: string | null;
  isAuthenticated: boolean;
  login: (response: AdminLoginResponse) => void;
  logout: () => void;
  hydrate: () => void;
}

export const useAdminAuthStore = create<AdminAuthState>()((set) => ({
  token: null, storeId: null, storeName: null, username: null, isAuthenticated: false,

  login: (res) => {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(res));
    set({ token: res.token, storeId: res.storeId, storeName: res.storeName, username: res.username, isAuthenticated: true });
  },

  logout: () => {
    localStorage.removeItem(STORAGE_KEY);
    set({ token: null, storeId: null, storeName: null, username: null, isAuthenticated: false });
  },

  hydrate: () => {
    const stored = localStorage.getItem(STORAGE_KEY);
    if (stored) {
      const data = JSON.parse(stored);
      set({ token: data.token, storeId: data.storeId, storeName: data.storeName, username: data.username, isAuthenticated: true });
    }
  },
}));
