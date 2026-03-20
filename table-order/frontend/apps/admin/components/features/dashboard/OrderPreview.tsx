'use client';

import { formatPrice, formatDateTime } from '@table-order/shared';
import { OrderStatusControl } from './OrderStatusControl';

export interface DashboardOrder {
  id: number;
  orderNo: string;
  status: string;
  totalAmount: number;
  createdAt: string;
  items: { menuName: string; quantity: number }[];
}

export function OrderPreview({ order, onChangeStatus }: {
  order: DashboardOrder;
  onChangeStatus: (orderId: number, status: string) => void;
}) {
  return (
    <div className="border-t pt-2 mt-2" data-testid={`order-preview-${order.id}`}>
      <div className="flex justify-between items-center mb-1">
        <span className="text-xs text-gray-500">#{order.orderNo} · {formatDateTime(order.createdAt)}</span>
        <OrderStatusControl status={order.status} onChangeStatus={(s) => onChangeStatus(order.id, s)} />
      </div>
      <ul className="text-xs text-gray-600">
        {order.items.slice(0, 3).map((item, i) => (
          <li key={i}>{item.menuName} × {item.quantity}</li>
        ))}
        {order.items.length > 3 && <li className="text-gray-400">외 {order.items.length - 3}건</li>}
      </ul>
      <p className="text-sm font-semibold text-right mt-1">{formatPrice(order.totalAmount)}</p>
    </div>
  );
}
