'use client';

import { Spinner, EmptyState, Badge } from '@table-order/ui';
import { formatPrice, formatDateTime } from '@table-order/shared';
import { useOrders } from '../../hooks/useOrders';

const STATUS_MAP: Record<string, { label: string; color: string }> = {
  PENDING: { label: '대기중', color: 'yellow' },
  PREPARING: { label: '준비중', color: 'blue' },
  COMPLETED: { label: '완료', color: 'green' },
};

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
          {orders.map((order) => {
            const info = STATUS_MAP[order.status] ?? { label: order.status, color: 'gray' };
            return (
              <div key={order.id} className="border rounded-lg p-4 bg-white" data-testid={`order-card-${order.id}`}>
                <div className="flex justify-between items-center mb-2">
                  <span className="text-sm font-semibold">#{order.id}</span>
                  <Badge color={info.color}>{info.label}</Badge>
                </div>
                <p className="text-xs text-gray-500 mb-2">{formatDateTime(order.createdAt)}</p>
                <ul className="text-sm space-y-1 mb-2">
                  {order.items.map((item, i) => (
                    <li key={i} className="flex justify-between">
                      <span>{item.menuName} × {item.quantity}</span>
                      <span>{formatPrice(item.unitPrice * item.quantity)}</span>
                    </li>
                  ))}
                </ul>
                <div className="border-t pt-2 flex justify-end">
                  <span className="font-bold text-[#FF9900]">{formatPrice(order.totalAmount)}</span>
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
}
