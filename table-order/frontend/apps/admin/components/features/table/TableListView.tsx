import { Button } from '@table-order/ui';
import type { StoreTable } from '@table-order/api-client';

export function TableListView({ tables, onComplete, onHistory }: {
  tables: StoreTable[];
  onComplete: (table: StoreTable) => void;
  onHistory: (table: StoreTable) => void;
}) {
  return (
    <div data-testid="table-list-view" className="border rounded-md overflow-hidden">
      <table className="w-full text-sm">
        <thead className="bg-gray-50"><tr><th className="px-4 py-2 text-left">번호</th><th className="px-4 py-2 text-left">상태</th><th className="px-4 py-2 text-right">관리</th></tr></thead>
        <tbody>
          {tables.map((t) => (
            <tr key={t.id} className="border-t">
              <td className="px-4 py-2">테이블 {t.tableNo}</td>
              <td className="px-4 py-2"><span className={t.hasActiveSession ? 'text-green-600' : 'text-gray-400'}>{t.hasActiveSession ? '활성' : '비활성'}</span></td>
              <td className="px-4 py-2 text-right flex gap-2 justify-end">
                {t.hasActiveSession && <Button size="sm" onClick={() => onComplete(t)} data-testid={`complete-${t.id}`}>이용 완료</Button>}
                <Button variant="secondary" size="sm" onClick={() => onHistory(t)} data-testid={`history-${t.id}`}>과거 내역</Button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
