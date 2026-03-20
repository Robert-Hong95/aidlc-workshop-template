'use client';

import { MenuItemRow } from './MenuItemRow';
import { EmptyState } from '@table-order/ui';
import type { Menu } from '@table-order/api-client';

export function MenuList({ menus, onEdit, onDelete }: {
  menus: Menu[];
  onEdit: (menu: Menu) => void;
  onDelete: (id: number) => void;
}) {
  if (menus.length === 0) return <EmptyState message="등록된 메뉴가 없습니다" />;

  return (
    <table className="w-full text-sm" data-testid="menu-list">
      <thead>
        <tr className="text-left text-gray-500 border-b">
          <th className="px-4 py-2">메뉴명</th>
          <th className="px-4 py-2">가격</th>
          <th className="px-4 py-2 text-right">관리</th>
        </tr>
      </thead>
      <tbody>
        {menus.map((m) => <MenuItemRow key={m.id} menu={m} onEdit={onEdit} onDelete={onDelete} />)}
      </tbody>
    </table>
  );
}
