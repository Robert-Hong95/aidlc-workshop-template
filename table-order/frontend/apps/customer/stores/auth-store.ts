import { create } from 'zustand';
import type { TableTokenResponse } from '@table-order/api-client';

const STORAGE_KEY = 'table-auth';

interface TableAuthState {
  accessToken: string | null;
  refreshToken: string | null;
  storeId: number | null;
  storeName: string | null;
  storeCode: string | null;
  tableId: number | null;
  tableNo: number | null;
  isAuthenticated: boolean;
  login: (response: TableTokenResponse, storeCode: string) => void;
  logout: () => void;
  setTokens: (access: string, refresh: string) => void;
  hydrate: () => void;
}

export const useTableAuthStore = create<TableAuthState>()((set) => ({
  accessToken: null,
  refreshToken: null,
  storeId: null,
  storeName: null,
  storeCode: null,
  tableId: null,
  tableNo: null,
  isAuthenticated: false,

  login: (response, storeCode) => {
    const { accessToken, refreshToken, storeId, storeName, tableId, tableNo } = response;
    const data = { accessToken, refreshToken, storeId, storeName, storeCode, tableId, tableNo };
    localStorage.setItem(STORAGE_KEY, JSON.stringify(data));
    set({ ...data, isAuthenticated: true });
  },

  logout: () => {
    localStorage.removeItem(STORAGE_KEY);
    set({ accessToken: null, refreshToken: null, storeId: null, storeName: null, storeCode: null, tableId: null, tableNo: null, isAuthenticated: false });
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
