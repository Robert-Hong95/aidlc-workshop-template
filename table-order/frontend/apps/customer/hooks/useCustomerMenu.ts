import { useState, useCallback } from 'react';
import { menuApi, type Category, type Menu, type CustomerMenuResponse } from '@table-order/api-client';

export interface MenuData {
  categories: (Category & { menus: Menu[] })[];
}

export function useCustomerMenu() {
  const [menuData, setMenuData] = useState<MenuData | null>(null);
  const [isLoading, setIsLoading] = useState(false);

  const fetchMenus = useCallback(async (storeId: number) => {
    setIsLoading(true);
    try {
      const data = await menuApi.customerList(storeId);
      setMenuData({
        categories: data.map((c) => ({
          id: c.categoryId, storeId: storeId, name: c.categoryName, displayOrder: c.displayOrder,
          menus: c.menus,
        })),
      });
    } finally {
      setIsLoading(false);
    }
  }, []);

  return { menuData, isLoading, fetchMenus };
}
