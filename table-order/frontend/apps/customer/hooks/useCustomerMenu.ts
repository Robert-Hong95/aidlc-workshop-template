import { useState, useCallback } from 'react';
import type { Category, Menu, CustomerMenuResponse } from '@table-order/api-client';

export interface MenuData {
  categories: (Category & { menus: Menu[] })[];
}

export function useCustomerMenu() {
  const [menuData, setMenuData] = useState<MenuData | null>(null);
  const [isLoading, setIsLoading] = useState(false);

  const fetchMenus = useCallback(async (_storeId: number) => {
    setIsLoading(true);
    try {
      // API deferred
    } finally {
      setIsLoading(false);
    }
  }, []);

  return { menuData, isLoading, fetchMenus };
}
