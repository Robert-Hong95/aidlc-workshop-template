'use client';

import { useEffect } from 'react';
import { Button, EmptyState } from '@table-order/ui';
import { CartItemRow } from '../../components/features/cart/CartItem';
import { CartSummary } from '../../components/features/cart/CartSummary';
import { useCartStore } from '../../stores/cart-store';

export default function CartPage() {
  const items = useCartStore((s) => s.items);
  const updateQuantity = useCartStore((s) => s.updateQuantity);
  const removeItem = useCartStore((s) => s.removeItem);
  const clear = useCartStore((s) => s.clear);
  const total = useCartStore((s) => s.total);

  useEffect(() => { useCartStore.getState().hydrate(); }, []);

  if (items.length === 0) {
    return (
      <div className="min-h-screen flex items-center justify-center" data-testid="cart-page">
        <EmptyState message="장바구니가 비어있습니다" action={<a href="/"><Button variant="secondary">메뉴 보기</Button></a>} />
      </div>
    );
  }

  return (
    <div className="min-h-screen pb-40" data-testid="cart-page">
      <div className="p-4">
        <div className="flex justify-between items-center mb-4">
          <h1 className="text-lg font-bold text-[#232F3E]">장바구니</h1>
          <button onClick={clear} className="text-sm text-red-500" data-testid="cart-clear-btn">전체 삭제</button>
        </div>
        {items.map((item) => (
          <CartItemRow key={item.menu.id} item={item} onUpdateQuantity={updateQuantity} onRemove={removeItem} />
        ))}
      </div>
      <CartSummary total={total()} itemCount={items.reduce((s, i) => s + i.quantity, 0)} onOrder={() => { window.location.href = '/order/confirm'; }} />
    </div>
  );
}
