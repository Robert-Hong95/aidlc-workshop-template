import { useState, useCallback } from 'react';
import { useAdminAuthStore } from '../stores/auth-store';
import { menuApi, type Menu, type CreateMenuRequest, type UpdateMenuRequest } from '@table-order/api-client';

export function useMenus() {
  const [menus, setMenus] = useState<Menu[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const token = useAdminAuthStore((s) => s.accessToken);
  const storeId = useAdminAuthStore((s) => s.storeId);

  const fetchMenus = useCallback(async (_categoryId?: number) => {
    if (!token || !storeId) return;
    setIsLoading(true);
    try { setMenus(await menuApi.list(token, storeId)); } finally { setIsLoading(false); }
  }, [token, storeId]);

  const createMenu = useCallback(async (data: CreateMenuRequest) => {
    if (!token || !storeId) return;
    await menuApi.create(token, storeId, data);
    await fetchMenus();
  }, [token, storeId, fetchMenus]);

  const updateMenu = useCallback(async (id: number, data: UpdateMenuRequest) => {
    if (!token) return;
    await menuApi.update(token, id, data);
    await fetchMenus();
  }, [token, fetchMenus]);

  const deleteMenu = useCallback(async (id: number) => {
    if (!token) return;
    await menuApi.delete(token, id);
    await fetchMenus();
  }, [token, fetchMenus]);

  const reorderMenus = useCallback(async (_ids: number[]) => {
    // reorder API deferred
  }, []);

  return { menus, isLoading, fetchMenus, createMenu, updateMenu, deleteMenu, reorderMenus };
}
