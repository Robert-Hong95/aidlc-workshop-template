'use client';

import { formatPrice } from '@table-order/shared';
import { QuantityControl } from './QuantityControl';
import type { CartItem } from '../../../stores/cart-store';

export function CartItemRow({ item, onUpdateQuantity, onRemove }: {
  item: CartItem;
  onUpdateQuantity: (menuId: number, qty: number) => void;
  onRemove: (menuId: number) => void;
}) {
  return (
    <div className="flex items-center justify-between py-3 border-b" data-testid={`cart-item-${item.menu.id}`}>
      <div className="flex-1">
        <p className="font-medium text-sm">{item.menu.name}</p>
        <p className="text-sm text-[#FF9900] font-semibold">{formatPrice(item.menu.price * item.quantity)}</p>
      </div>
      <div className="flex items-center gap-3">
        <QuantityControl quantity={item.quantity} onChange={(qty) => onUpdateQuantity(item.menu.id, qty)} />
        <button onClick={() => onRemove(item.menu.id)} className="text-red-500 text-sm min-w-[44px] min-h-[44px]" data-testid="cart-remove-btn">삭제</button>
      </div>
    </div>
  );
}
