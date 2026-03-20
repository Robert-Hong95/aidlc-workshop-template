'use client';

import { useEffect } from 'react';
import { Spinner, EmptyState } from '@table-order/ui';
import { OrderCard } from '../../components/features/order/OrderCard';
import { useOrders } from '../../hooks/useOrders';
import { useTableAuthStore } from '../../stores/auth-store';

export default function OrderListPage() {
  const { orders, isLoading, fetchOrders } = useOrders();
  const isAuthenticated = useTableAuthStore((s) => s.isAuthenticated);

  useEffect(() => {
    useTableAuthStore.getState().hydrate();
  }, []);

  useEffect(() => {
    if (isAuthenticated) fetchOrders();
  }, [isAuthenticated, fetchOrders]);

  if (isLoading) {
    return <div className="flex items-center justify-center min-h-screen"><Spinner size="lg" /></div>;
  }

  return (
    <div className="min-h-screen pb-20 p-4" data-testid="order-list-page">
      <h1 className="text-lg font-bold text-[#232F3E] mb-4">주문 내역</h1>
      {orders.length === 0 ? (
        <EmptyState message="주문 내역이 없습니다" />
      ) : (
        <div className="flex flex-col gap-3">
          {orders.map((order) => <OrderCard key={order.id} order={order} />)}
        </div>
      )}
      <nav className="fixed bottom-0 left-0 right-0 bg-white border-t flex">
        <a href="/" className="flex-1 py-3 text-center text-sm font-medium text-gray-500">🍽 메뉴</a>
        <a href="/cart" className="flex-1 py-3 text-center text-sm font-medium text-gray-500">🛒 장바구니</a>
        <a href="/orders" className="flex-1 py-3 text-center text-sm font-medium text-[#FF9900]">📋 주문내역</a>
      </nav>
    </div>
  );
}
