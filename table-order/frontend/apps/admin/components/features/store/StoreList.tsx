import { Button } from '@table-order/ui';
import type { Store } from '@table-order/api-client';

export function StoreList({ stores, onEdit }: {
  stores: Store[];
  onEdit: (store: Store) => void;
}) {
  return (
    <div data-testid="store-list" className="border rounded-md overflow-hidden">
      <table className="w-full text-sm">
        <thead className="bg-gray-50">
          <tr>
            <th className="px-4 py-2 text-left">매장명</th>
            <th className="px-4 py-2 text-left">코드</th>
            <th className="px-4 py-2 text-left">주소</th>
            <th className="px-4 py-2 text-right">관리</th>
          </tr>
        </thead>
        <tbody>
          {stores.map((store) => (
            <tr key={store.id} className="border-t">
              <td className="px-4 py-2">{store.name}</td>
              <td className="px-4 py-2 text-gray-500">{store.code}</td>
              <td className="px-4 py-2 text-gray-500">{store.address ?? '-'}</td>
              <td className="px-4 py-2 text-right">
                <Button variant="secondary" size="sm" onClick={() => onEdit(store)} data-testid="store-edit-btn">수정</Button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
