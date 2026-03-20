'use client';

import { Modal, Button } from '@table-order/ui';
import { formatPrice, formatDateTime } from '@table-order/shared';
import { OrderStatusControl } from './OrderStatusControl';
import type { DashboardOrder } from './OrderPreview';

export function OrderDetailModal({ isOpen, order, onClose, onChangeStatus, onDelete }: {
  isOpen: boolean;
  order: DashboardOrder | null;
  onClose: () => void;
  onChangeStatus: (orderId: number, status: string) => void;
  onDelete: (orderId: number) => void;
}) {
  if (!order) return null;

  return (
    <Modal isOpen={isOpen} onClose={onClose} title={`주문 #${order.orderNo}`} data-testid="order-detail-modal">
      <div className="space-y-3">
        <div className="flex justify-between items-center">
          <span className="text-xs text-gray-500">{formatDateTime(order.createdAt)}</span>
          <OrderStatusControl status={order.status} onChangeStatus={(s) => onChangeStatus(order.id, s)} />
        </div>
        <ul className="text-sm space-y-1 border-t pt-2">
          {order.items.map((item, i) => (
            <li key={i} className="flex justify-between">
              <span>{item.menuName} × {item.quantity}</span>
            </li>
          ))}
        </ul>
        <div className="border-t pt-2 flex justify-between items-center">
          <span className="font-bold text-[#FF9900]">{formatPrice(order.totalAmount)}</span>
          <Button variant="danger" size="sm" onClick={() => onDelete(order.id)} data-testid="order-delete-btn">삭제</Button>
        </div>
      </div>
    </Modal>
  );
}
