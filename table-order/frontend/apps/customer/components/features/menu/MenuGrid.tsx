'use client';

import { MenuCard } from './MenuCard';
import type { Menu } from '@table-order/api-client';

export function MenuGrid({ menus, onAdd }: {
  menus: Menu[];
  onAdd: (menu: Menu) => void;
}) {
  return (
    <div className="grid grid-cols-2 gap-3 p-4" data-testid="menu-grid">
      {menus.map((menu) => (
        <MenuCard key={menu.id} menu={menu} onAdd={onAdd} />
      ))}
    </div>
  );
}
