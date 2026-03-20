'use client';

import { useState, useEffect } from 'react';
import { EmptyState, Spinner } from '@table-order/ui';
import { CategoryTabs } from '../components/features/menu/CategoryTabs';
import { MenuGrid } from '../components/features/menu/MenuGrid';
import { useCustomerMenu } from '../hooks/useCustomerMenu';
import { useTableAuth } from '../hooks/useTableAuth';
import { useCartStore } from '../stores/cart-store';
import { useTableAuthStore } from '../stores/auth-store';
import { SetupForm } from '../components/features/setup/SetupForm';
import type { Menu } from '@table-order/api-client';

export default function MenuPage() {
  const { menuData, isLoading: menuLoading, fetchMenus } = useCustomerMenu();
  const { login, isAuthenticated, isLoading: authLoading, error: authError, autoLogin } = useTableAuth();
  const addItem = useCartStore((s) => s.addItem);
  const cartItems = useCartStore((s) => s.items);
  const storeId = useTableAuthStore((s) => s.storeId);
  const storeName = useTableAuthStore((s) => s.storeName);
  const tableNo = useTableAuthStore((s) => s.tableNo);
  const [activeCategoryId, setActiveCategoryId] = useState<number | null>(null);
  const [hydrated, setHydrated] = useState(false);

  // hydrate auth & cart on mount
  useEffect(() => {
    useCartStore.getState().hydrate();
    useTableAuthStore.getState().hydrate();
    setHydrated(true);
  }, []);

  // fetch menus when authenticated
  useEffect(() => {
    if (hydrated && isAuthenticated && storeId) {
      fetchMenus(storeId);
    }
  }, [hydrated, isAuthenticated, storeId, fetchMenus]);

  useEffect(() => {
    if (menuData?.categories.length && !activeCategoryId) {
      setActiveCategoryId(menuData.categories[0].id);
    }
  }, [menuData, activeCategoryId]);

  if (!hydrated) {
    return <div className="flex items-center justify-center min-h-screen"><Spinner size="lg" /></div>;
  }

  // 로그인 안 된 상태 → 초기 설정 화면
  if (!isAuthenticated) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50 p-4">
        <div className="w-full max-w-sm bg-white rounded-lg shadow p-6">
          <h1 className="text-xl font-bold text-[#232F3E] mb-2 text-center">테이블오더</h1>
          <p className="text-sm text-gray-500 mb-6 text-center">테이블 초기 설정</p>
          <SetupForm
            onSubmit={(data) => login(data.storeCode, data.tableNo, data.password)}
            isLoading={authLoading}
            error={authError ?? undefined}
          />
        </div>
      </div>
    );
  }

  const activeMenus: Menu[] = menuData?.categories.find((c) => c.id === activeCategoryId)?.menus ?? [];
  const cartCount = cartItems.reduce((sum, i) => sum + i.quantity, 0);

  if (menuLoading) {
    return <div className="flex items-center justify-center min-h-screen"><Spinner size="lg" /></div>;
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
      <div className="bg-[#232F3E] text-white px-4 py-3 flex justify-between items-center">
        <div>
          <span className="font-bold">{storeName}</span>
          <span className="ml-2 text-sm text-gray-300">테이블 {tableNo}</span>
        </div>
        <a href="/cart" className="relative bg-[#FF9900] px-3 py-1.5 rounded text-sm font-medium">
          🛒 장바구니{cartCount > 0 && <span className="ml-1">({cartCount})</span>}
        </a>
      </div>
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
      <nav className="fixed bottom-0 left-0 right-0 bg-white border-t flex">
        <a href="/" className="flex-1 py-3 text-center text-sm font-medium text-[#FF9900]">🍽 메뉴</a>
        <a href="/cart" className="flex-1 py-3 text-center text-sm font-medium text-gray-500">🛒 장바구니</a>
        <a href="/orders" className="flex-1 py-3 text-center text-sm font-medium text-gray-500">📋 주문내역</a>
      </nav>
    </div>
  );
}
