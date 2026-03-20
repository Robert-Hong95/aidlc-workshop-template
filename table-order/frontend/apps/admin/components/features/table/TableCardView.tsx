import { Button } from '@table-order/ui';
import { formatPrice } from '@table-order/shared';
import type { StoreTable } from '@table-order/api-client';

export function TableCardView({ tables, onComplete, onHistory }: {
  tables: StoreTable[];
  onComplete: (table: StoreTable) => void;
  onHistory: (table: StoreTable) => void;
}) {
  return (
    <div data-testid="table-card-view" className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
      {tables.map((t) => (
        <div key={t.id} className={`border rounded-lg p-4 ${t.sessionActive ? 'border-green-300 bg-green-50' : 'border-gray-200'}`}>
          <div className="flex justify-between items-center mb-2">
            <span className="font-semibold text-[#232F3E]">테이블 {t.tableNo}</span>
            <span className={`text-xs px-2 py-0.5 rounded-full ${t.sessionActive ? 'bg-green-100 text-green-700' : 'bg-gray-100 text-gray-500'}`}>{t.sessionActive ? '활성' : '비활성'}</span>
          </div>
          <p className="text-sm text-gray-600 mb-3">{formatPrice(t.totalOrderAmount)}</p>
          <div className="flex gap-2">
            {t.sessionActive && <Button size="sm" onClick={() => onComplete(t)}>완료</Button>}
            <Button variant="secondary" size="sm" onClick={() => onHistory(t)}>내역</Button>
          </div>
        </div>
      ))}
    </div>
  );
}
