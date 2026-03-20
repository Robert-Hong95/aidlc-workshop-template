'use client';

import { Button } from '@table-order/ui';
import { formatPrice } from '@table-order/shared';

export function CartSummary({ total, itemCount, onOrder }: {
  total: number;
  itemCount: number;
  onOrder: () => void;
}) {
  return (
    <div className="fixed bottom-16 left-0 right-0 bg-white border-t p-4 shadow-lg" data-testid="cart-summary">
      <div className="flex justify-between items-center mb-3">
        <span className="text-sm text-gray-600">총 {itemCount}개</span>
        <span className="text-lg font-bold text-[#232F3E]">{formatPrice(total)}</span>
      </div>
      <Button className="w-full" onClick={onOrder} data-testid="order-btn">주문하기</Button>
    </div>
  );
}
