import { Button } from '@table-order/ui';
import type { Category } from '@table-order/api-client';

export function CategoryManager({ categories, onAdd, onEdit, onDelete }: {
  categories: Category[];
  onAdd: () => void;
  onEdit: (cat: Category) => void;
  onDelete: (id: number) => void;
}) {
  return (
    <div data-testid="category-manager" className="flex flex-col gap-2">
      <div className="flex justify-between items-center">
        <h3 className="font-semibold text-[#232F3E]">카테고리</h3>
        <Button size="sm" onClick={onAdd} data-testid="cat-add-btn">추가</Button>
      </div>
      <ul className="flex flex-col gap-1">
        {categories.map((cat) => (
          <li key={cat.id} className="flex justify-between items-center px-3 py-2 border rounded-md">
            <span>{cat.name}</span>
            <div className="flex gap-2">
              <button onClick={() => onEdit(cat)} className="text-sm text-blue-600" data-testid={`cat-edit-${cat.id}`}>수정</button>
              <button onClick={() => onDelete(cat.id)} className="text-sm text-red-600" data-testid={`cat-delete-${cat.id}`}>삭제</button>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
}
