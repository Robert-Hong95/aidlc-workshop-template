import { create } from 'zustand';
import type { TableLoginResponse } from '@table-order/api-client';

const STORAGE_KEY = 'table-auth';

interface TableAuthState {
  token: string | null;
  storeId: number | null;
  storeName: string | null;
  storeCode: string | null;
  tableId: number | null;
  tableNo: number | null;
  isAuthenticated: boolean;
  login: (response: TableLoginResponse, storeCode: string) => void;
  logout: () => void;
  hydrate: () => void;
}

export const useTableAuthStore = create<TableAuthState>()((set) => ({
  token: null, storeId: null, storeName: null, storeCode: null, tableId: null, tableNo: null, isAuthenticated: false,

  login: (res, storeCode) => {
    const data = { token: res.token, storeId: res.storeId, storeName: res.storeName, storeCode, tableId: res.tableId, tableNo: res.tableNo };
    localStorage.setItem(STORAGE_KEY, JSON.stringify(data));
    set({ ...data, isAuthenticated: true });
  },

  logout: () => {
    localStorage.removeItem(STORAGE_KEY);
    set({ token: null, storeId: null, storeName: null, storeCode: null, tableId: null, tableNo: null, isAuthenticated: false });
  },

  hydrate: () => {
    const stored = localStorage.getItem(STORAGE_KEY);
    if (stored) {
      const data = JSON.parse(stored);
      set({ ...data, isAuthenticated: true });
    }
  },
}));
