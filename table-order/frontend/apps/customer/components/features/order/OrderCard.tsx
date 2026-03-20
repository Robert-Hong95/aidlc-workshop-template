'use client';

import { formatPrice, formatDateTime } from '@table-order/shared';
import { OrderStatusBadge } from './OrderStatusBadge';

export interface OrderData {
  id: number;
  orderNo: string;
  status: string;
  totalAmount: number;
  createdAt: string;
  items: { menuName: string; quantity: number; price: number }[];
}

export function OrderCard({ order }: { order: OrderData }) {
  return (
    <div className="border rounded-lg p-4 bg-white" data-testid={`order-card-${order.id}`}>
      <div className="flex justify-between items-center mb-2">
        <span className="text-sm font-semibold text-[#232F3E]">#{order.orderNo}</span>
        <OrderStatusBadge status={order.status} />
      </div>
      <p className="text-xs text-gray-500 mb-2">{formatDateTime(order.createdAt)}</p>
      <ul className="text-sm space-y-1 mb-2">
        {order.items.map((item, i) => (
          <li key={i} className="flex justify-between">
            <span>{item.menuName} × {item.quantity}</span>
            <span>{formatPrice(item.price * item.quantity)}</span>
          </li>
        ))}
      </ul>
      <div className="border-t pt-2 flex justify-end">
        <span className="font-bold text-[#FF9900]">{formatPrice(order.totalAmount)}</span>
      </div>
    </div>
  );
}
