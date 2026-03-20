'use client';

import type { Category } from '@table-order/api-client';

export function CategoryTabs({ categories, activeId, onSelect }: {
  categories: Category[];
  activeId: number | null;
  onSelect: (id: number) => void;
}) {
  return (
    <nav className="sticky top-0 z-10 bg-white border-b overflow-x-auto" data-testid="category-tabs">
      <div className="flex gap-1 px-4 py-2 min-w-max">
        {categories.map((cat) => (
          <button
            key={cat.id}
            onClick={() => onSelect(cat.id)}
            className={`px-4 py-2 rounded-full text-sm whitespace-nowrap min-h-[44px] ${activeId === cat.id ? 'bg-[#FF9900] text-white font-semibold' : 'bg-gray-100 text-gray-700'}`}
            data-testid={`cat-tab-${cat.id}`}
          >
            {cat.name}
          </button>
        ))}
      </div>
    </nav>
  );
}
