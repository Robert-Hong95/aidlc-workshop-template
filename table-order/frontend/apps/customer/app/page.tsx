'use client';

import { useState, useEffect } from 'react';
import { EmptyState, Spinner } from '@table-order/ui';
import { CategoryTabs } from '../components/features/menu/CategoryTabs';
import { MenuGrid } from '../components/features/menu/MenuGrid';
import { useCustomerMenu } from '../hooks/useCustomerMenu';
import { useCartStore } from '../stores/cart-store';

export default function MenuPage() {
  const { menuData, isLoading } = useCustomerMenu();
  const addItem = useCartStore((s) => s.addItem);
  const [activeCategoryId, setActiveCategoryId] = useState<number | null>(null);

  useEffect(() => { useCartStore.getState().hydrate(); }, []);

  useEffect(() => {
    if (menuData?.length && !activeCategoryId) {
      setActiveCategoryId(menuData[0].categoryId);
    }
  }, [menuData, activeCategoryId]);

  if (isLoading) {
    return <div className="flex items-center justify-center min-h-screen"><Spinner size="lg" /></div>;
  }

  if (!menuData || menuData.length === 0) {
    return <div className="min-h-screen flex items-center justify-center" data-testid="menu-page"><EmptyState message="등록된 메뉴가 없습니다" /></div>;
  }

  const categories = menuData.map((c) => ({ id: c.categoryId, storeId: 0, name: c.categoryName, displayOrder: c.displayOrder }));
  const activeMenus = menuData.find((c) => c.categoryId === activeCategoryId)?.menus ?? [];

  return (
    <div className="min-h-screen pb-20" data-testid="menu-page">
      <CategoryTabs categories={categories} activeId={activeCategoryId} onSelect={setActiveCategoryId} />
      {activeMenus.length > 0 ? (
        <MenuGrid menus={activeMenus} onAdd={addItem} />
      ) : (
        <EmptyState message="이 카테고리에 메뉴가 없습니다" />
      )}
    </div>
  );
}
