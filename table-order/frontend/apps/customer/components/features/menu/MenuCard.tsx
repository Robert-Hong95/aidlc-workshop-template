'use client';

import { Button } from '@table-order/ui';
import { formatPrice } from '@table-order/shared';
import type { Menu } from '@table-order/api-client';

const PLACEHOLDER_IMG = '/placeholder-menu.svg';

export function MenuCard({ menu, onAdd }: {
  menu: Menu;
  onAdd: (menu: Menu) => void;
}) {
  return (
    <div className="border rounded-lg overflow-hidden bg-white" data-testid={`menu-card-${menu.id}`}>
      <img
        src={menu.imageUrl || PLACEHOLDER_IMG}
        alt={menu.name}
        className="w-full h-36 object-cover bg-gray-100"
        loading="lazy"
      />
      <div className="p-3 flex flex-col gap-1">
        <h3 className="font-semibold text-sm text-[#232F3E]">{menu.name}</h3>
        {menu.description && <p className="text-xs text-gray-500 line-clamp-2">{menu.description}</p>}
        <div className="flex justify-between items-center mt-2">
          <span className="font-bold text-[#FF9900]">{formatPrice(menu.price)}</span>
          <Button size="sm" onClick={() => onAdd(menu)} data-testid={`add-btn-${menu.id}`} className="min-w-[44px] min-h-[44px]">
            담기
          </Button>
        </div>
      </div>
    </div>
  );
}
