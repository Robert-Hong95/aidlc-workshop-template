import { create } from 'zustand';
import type { Menu } from '@table-order/api-client';

const STORAGE_KEY = 'cart';

export interface CartItem {
  menu: Menu;
  quantity: number;
}

interface CartState {
  items: CartItem[];
  addItem: (menu: Menu) => void;
  removeItem: (menuId: number) => void;
  updateQuantity: (menuId: number, quantity: number) => void;
  clear: () => void;
  total: () => number;
  hydrate: () => void;
}

function persist(items: CartItem[]) {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(items));
}

export const useCartStore = create<CartState>()((set, get) => ({
  items: [],

  addItem: (menu) => {
    const items = get().items;
    const existing = items.find((i) => i.menu.id === menu.id);
    const next = existing
      ? items.map((i) => i.menu.id === menu.id ? { ...i, quantity: i.quantity + 1 } : i)
      : [...items, { menu, quantity: 1 }];
    persist(next);
    set({ items: next });
  },

  removeItem: (menuId) => {
    const next = get().items.filter((i) => i.menu.id !== menuId);
    persist(next);
    set({ items: next });
  },

  updateQuantity: (menuId, quantity) => {
    if (quantity <= 0) {
      get().removeItem(menuId);
      return;
    }
    const next = get().items.map((i) => i.menu.id === menuId ? { ...i, quantity } : i);
    persist(next);
    set({ items: next });
  },

  clear: () => {
    localStorage.removeItem(STORAGE_KEY);
    set({ items: [] });
  },

  total: () => get().items.reduce((sum, i) => sum + i.menu.price * i.quantity, 0),

  hydrate: () => {
    const stored = localStorage.getItem(STORAGE_KEY);
    if (stored) {
      try { set({ items: JSON.parse(stored) }); } catch { /* ignore */ }
    }
  },
}));
