'use client';

import { Button } from '@table-order/ui';

export function QuantityControl({ quantity, onChange }: {
  quantity: number;
  onChange: (qty: number) => void;
}) {
  return (
    <div className="flex items-center gap-2" data-testid="quantity-control">
      <button
        onClick={() => onChange(quantity - 1)}
        className="w-8 h-8 rounded-full border flex items-center justify-center text-lg min-w-[44px] min-h-[44px]"
        data-testid="qty-decrease"
      >
        −
      </button>
      <span className="w-8 text-center font-semibold" data-testid="qty-value">{quantity}</span>
      <button
        onClick={() => onChange(quantity + 1)}
        className="w-8 h-8 rounded-full border flex items-center justify-center text-lg min-w-[44px] min-h-[44px]"
        data-testid="qty-increase"
      >
        +
      </button>
    </div>
  );
}
