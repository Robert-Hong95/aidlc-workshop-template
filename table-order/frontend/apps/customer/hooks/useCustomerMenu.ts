import { useState, useCallback } from 'react';
import type { Category, Menu, CustomerMenuResponse } from '@table-order/api-client';

const API = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

export interface MenuData {
  categories: (Category & { menus: Menu[] })[];
}

export function useCustomerMenu() {
  const [menuData, setMenuData] = useState<MenuData | null>(null);
  const [isLoading, setIsLoading] = useState(false);

  const fetchMenus = useCallback(async (storeId: number) => {
    setIsLoading(true);
    try {
      const res = await fetch(`${API}/api/customer/stores/${storeId}/menus`);
      const json = await res.json();
      if (json.success && json.data) {
        const categories = (json.data as CustomerMenuResponse[]).map((c) => ({
          id: c.categoryId,
          storeId: storeId,
          name: c.categoryName,
          displayOrder: c.displayOrder,
          menus: c.menus,
        }));
        setMenuData({ categories });
      }
    } finally {
      setIsLoading(false);
    }
  }, []);

  return { menuData, isLoading, fetchMenus };
}
