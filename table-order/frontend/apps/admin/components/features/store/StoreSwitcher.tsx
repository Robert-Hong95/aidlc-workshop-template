import { useState } from 'react';
import type { Store } from '@table-order/api-client';

export function StoreSwitcher({ stores, currentStoreId, onSwitch }: {
  stores: Store[];
  currentStoreId: number;
  onSwitch: (storeId: number) => void;
}) {
  const [open, setOpen] = useState(false);
  const current = stores.find((s) => s.id === currentStoreId);

  return (
    <div className="relative">
      <button data-testid="store-switcher" onClick={() => setOpen(!open)} className="flex items-center gap-1 font-semibold hover:text-[#FF9900]">
        {current?.name ?? '매장 선택'} <span className="text-xs">▼</span>
      </button>
      {open && (
        <div data-testid="store-dropdown" className="absolute top-full left-0 mt-1 bg-white border rounded-md shadow-lg z-50 min-w-[160px]">
          {stores.map((store) => (
            <button
              key={store.id}
              onClick={() => { onSwitch(store.id); setOpen(false); }}
              className={`block w-full text-left px-4 py-2 text-sm hover:bg-gray-100 ${store.id === currentStoreId ? 'bg-gray-50 font-medium' : ''}`}
            >
              {store.name}
            </button>
          ))}
        </div>
      )}
    </div>
  );
}
