'use client';

import { Spinner, EmptyState } from '@table-order/ui';
import { OrderCard } from '../../components/features/order/OrderCard';
import { useOrders } from '../../hooks/useOrders';

export default function OrderListPage() {
  const { orders, isLoading } = useOrders();

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
    </div>
  );
}
