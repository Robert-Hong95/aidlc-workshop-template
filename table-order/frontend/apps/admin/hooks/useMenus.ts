import { useState, useCallback } from 'react';
import type { Menu, CreateMenuRequest, UpdateMenuRequest } from '@table-order/api-client';

export function useMenus() {
  const [menus, setMenus] = useState<Menu[]>([]);
  const [isLoading, setIsLoading] = useState(false);

  const fetchMenus = useCallback(async (_categoryId: number) => {
    setIsLoading(true);
    try {
      // API deferred
    } finally {
      setIsLoading(false);
    }
  }, []);

  const createMenu = useCallback(async (_data: CreateMenuRequest) => {
    // API deferred
  }, []);

  const updateMenu = useCallback(async (_id: number, _data: UpdateMenuRequest) => {
    // API deferred
  }, []);

  const deleteMenu = useCallback(async (_id: number) => {
    // API deferred
  }, []);

  const reorderMenus = useCallback(async (_ids: number[]) => {
    // API deferred
  }, []);

  return { menus, isLoading, fetchMenus, createMenu, updateMenu, deleteMenu, reorderMenus };
}
