'use client';

import { useState, useEffect } from 'react';
import { EmptyState, Spinner } from '@table-order/ui';
import { CategoryTabs } from '../components/features/menu/CategoryTabs';
import { MenuGrid } from '../components/features/menu/MenuGrid';
import { useCustomerMenu } from '../hooks/useCustomerMenu';
import { useCartStore } from '../stores/cart-store';
import { useTableAuthStore } from '../stores/auth-store';
import type { Menu } from '@table-order/api-client';

export default function MenuPage() {
  const { menuData, isLoading } = useCustomerMenu();
  const addItem = useCartStore((s) => s.addItem);
  const cartItems = useCartStore((s) => s.items);
  const storeId = useTableAuthStore((s) => s.storeId);
  const [activeCategoryId, setActiveCategoryId] = useState<number | null>(null);

  useEffect(() => {
    useCartStore.getState().hydrate();
  }, []);

  useEffect(() => {
    if (menuData?.categories.length && !activeCategoryId) {
      setActiveCategoryId(menuData.categories[0].id);
    }
  }, [menuData, activeCategoryId]);

  const activeMenus: Menu[] = menuData?.categories.find((c) => c.id === activeCategoryId)?.menus ?? [];
  const cartCount = cartItems.reduce((sum, i) => sum + i.quantity, 0);

  if (isLoading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <Spinner size="lg" />
      </div>
    );
  }

  if (!menuData || menuData.categories.length === 0) {
    return (
      <div className="min-h-screen flex items-center justify-center" data-testid="menu-page">
        <EmptyState message="등록된 메뉴가 없습니다" />
      </div>
    );
  }

  return (
    <div className="min-h-screen pb-20" data-testid="menu-page">
      <CategoryTabs
        categories={menuData.categories}
        activeId={activeCategoryId}
        onSelect={setActiveCategoryId}
      />
      {activeMenus.length > 0 ? (
        <MenuGrid menus={activeMenus} onAdd={addItem} />
      ) : (
        <EmptyState message="이 카테고리에 메뉴가 없습니다" />
      )}
    </div>
  );
}
