'use client';

import { useState, useEffect } from 'react';
import { Button, EmptyState } from '@table-order/ui';
import { formatPrice } from '@table-order/shared';
import { useCartStore } from '../../../stores/cart-store';
import { useOrders } from '../../../hooks/useOrders';
import { OrderSuccessModal } from '../../../components/features/order/OrderSuccessModal';

export default function OrderConfirmPage() {
  const items = useCartStore((s) => s.items);
  const total = useCartStore((s) => s.total);
  const clear = useCartStore((s) => s.clear);
  const { createOrder, isLoading } = useOrders();
  const [orderNo, setOrderNo] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => { useCartStore.getState().hydrate(); }, []);

  if (items.length === 0 && !orderNo) {
    return (
      <div className="min-h-screen flex items-center justify-center" data-testid="order-confirm-page">
        <EmptyState message="주문할 메뉴가 없습니다" action={<a href="/"><Button variant="secondary">메뉴 보기</Button></a>} />
      </div>
    );
  }

  const handleOrder = async () => {
    setError(null);
    const result = await createOrder({
      storeId: 0, // API 연동 시 실제 값
      tableId: 0,
      items: items.map((i) => ({ menuId: i.menu.id, menuName: i.menu.name, quantity: i.quantity, price: i.menu.price })),
    });
    if (result) {
      setOrderNo(result.orderNo);
      clear();
    } else {
      setError('주문에 실패했습니다. 다시 시도해주세요.');
    }
  };

  return (
    <div className="min-h-screen p-4 pb-32" data-testid="order-confirm-page">
      <h1 className="text-lg font-bold text-[#232F3E] mb-4">주문 확인</h1>
      <div className="border rounded-lg p-4 bg-white mb-4">
        {items.map((item) => (
          <div key={item.menu.id} className="flex justify-between py-2 border-b last:border-0 text-sm">
            <span>{item.menu.name} × {item.quantity}</span>
            <span className="font-semibold">{formatPrice(item.menu.price * item.quantity)}</span>
          </div>
        ))}
        <div className="flex justify-between pt-3 font-bold">
          <span>합계</span>
          <span className="text-[#FF9900]">{formatPrice(total())}</span>
        </div>
      </div>
      {error && <p className="text-red-500 text-sm mb-4" data-testid="order-error">{error}</p>}
      <div className="flex gap-3">
        <Button variant="secondary" onClick={() => { window.location.href = '/cart'; }} className="flex-1">뒤로</Button>
        <Button onClick={handleOrder} loading={isLoading} disabled={isLoading} className="flex-1" data-testid="confirm-order-btn">주문 확정</Button>
      </div>
      <OrderSuccessModal isOpen={!!orderNo} orderNo={orderNo ?? ''} onClose={() => { window.location.href = '/'; }} />
    </div>
  );
}
