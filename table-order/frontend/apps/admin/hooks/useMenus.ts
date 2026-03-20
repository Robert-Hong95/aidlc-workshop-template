import { useState, useCallback } from 'react';
import type { Menu, CreateMenuRequest, UpdateMenuRequest } from '@table-order/api-client';
import { useAdminAuthStore } from '../stores/auth-store';

const API = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

function authHeaders() {
  const token = useAdminAuthStore.getState().accessToken;
  return { 'Content-Type': 'application/json', ...(token ? { Authorization: `Bearer ${token}` } : {}) };
}

export function useMenus() {
  const [menus, setMenus] = useState<Menu[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [storeIdRef, setStoreIdRef] = useState<number | null>(null);

  const fetchMenus = useCallback(async (storeId: number) => {
    setStoreIdRef(storeId);
    setIsLoading(true);
    try {
      const res = await fetch(`${API}/api/admin/stores/${storeId}/menus`, { headers: authHeaders() });
      const json = await res.json();
      if (json.success) setMenus(json.data || []);
    } finally { setIsLoading(false); }
  }, []);

  const createMenu = useCallback(async (data: CreateMenuRequest) => {
    if (!storeIdRef) return;
    setIsLoading(true);
    try {
      await fetch(`${API}/api/admin/stores/${storeIdRef}/menus`, {
        method: 'POST', headers: authHeaders(), body: JSON.stringify(data),
      });
      await fetchMenus(storeIdRef);
    } finally { setIsLoading(false); }
  }, [storeIdRef, fetchMenus]);

  const updateMenu = useCallback(async (id: number, data: UpdateMenuRequest) => {
    setIsLoading(true);
    try {
      await fetch(`${API}/api/admin/menus/${id}`, {
        method: 'PUT', headers: authHeaders(), body: JSON.stringify(data),
      });
      if (storeIdRef) await fetchMenus(storeIdRef);
    } finally { setIsLoading(false); }
  }, [storeIdRef, fetchMenus]);

  const deleteMenu = useCallback(async (id: number) => {
    await fetch(`${API}/api/admin/menus/${id}`, { method: 'DELETE', headers: authHeaders() });
    if (storeIdRef) await fetchMenus(storeIdRef);
  }, [storeIdRef, fetchMenus]);

  return { menus, isLoading, fetchMenus, createMenu, updateMenu, deleteMenu };
}
