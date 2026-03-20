import { Button } from '@table-order/ui';
import { formatPrice } from '@table-order/shared';
import type { Menu } from '@table-order/api-client';

export function MenuItemRow({ menu, onEdit, onDelete }: {
  menu: Menu;
  onEdit: (menu: Menu) => void;
  onDelete: (id: number) => void;
}) {
  return (
    <tr className="border-t">
      <td className="px-4 py-2">{menu.name}</td>
      <td className="px-4 py-2">{formatPrice(menu.price)}</td>
      <td className="px-4 py-2 text-right flex gap-2 justify-end">
        <Button variant="secondary" size="sm" onClick={() => onEdit(menu)} data-testid="menu-edit-btn">수정</Button>
        <Button variant="danger" size="sm" onClick={() => onDelete(menu.id)} data-testid="menu-delete-btn">삭제</Button>
      </td>
    </tr>
  );
}
