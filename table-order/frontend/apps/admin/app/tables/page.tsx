'use client';

import { useEffect, useState } from 'react';
import { Button, ConfirmDialog, Spinner, EmptyState } from '@table-order/ui';
import { useTables } from '../../hooks/useTables';
import { useAdminAuthStore } from '../../stores/auth-store';

export default function TablesPage() {
  const storeId = useAdminAuthStore((s) => s.storeId);
  const { tables, isLoading, fetchTables, createTable, completeSession } = useTables(storeId ?? 0);
  const [showForm, setShowForm] = useState(false);
  const [tableNo, setTableNo] = useState('');
  const [password, setPassword] = useState('');
  const [endTarget, setEndTarget] = useState<number | null>(null);

  useEffect(() => {
    useAdminAuthStore.getState().hydrate();
  }, []);

  useEffect(() => {
    if (storeId) fetchTables();
  }, [storeId, fetchTables]);

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!storeId) return;
    await createTable({ storeId, tableNo: Number(tableNo), password });
    setShowForm(false);
    setTableNo('');
    setPassword('');
  };

  if (isLoading) {
    return <div className="flex items-center justify-center min-h-[400px]"><Spinner size="lg" /></div>;
  }

  return (
    <div data-testid="tables-page">
      <div className="flex justify-between items-center mb-4">
        <h1 className="text-xl font-bold text-[#232F3E]">테이블 관리</h1>
        <Button onClick={() => setShowForm(!showForm)}>테이블 추가</Button>
      </div>

      {showForm && (
        <form onSubmit={handleCreate} className="border rounded-lg p-4 bg-white mb-4 flex gap-3 items-end">
          <div>
            <label className="block text-sm font-medium mb-1">테이블 번호</label>
            <input type="number" value={tableNo} onChange={(e) => setTableNo(e.target.value)} className="border rounded px-3 py-2 w-32" required min={1} />
          </div>
          <div>
            <label className="block text-sm font-medium mb-1">비밀번호</label>
            <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} className="border rounded px-3 py-2 w-40" required />
          </div>
          <Button type="submit">추가</Button>
        </form>
      )}

      {tables.length === 0 ? (
        <EmptyState message="등록된 테이블이 없습니다" />
      ) : (
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
          {tables.map((t) => (
            <div key={t.id} className="border rounded-lg p-4 bg-white">
              <div className="font-bold text-lg mb-2">테이블 {t.tableNo}</div>
              <div className={`text-sm mb-3 ${t.hasActiveSession ? 'text-green-600' : 'text-gray-400'}`}>
                {t.hasActiveSession ? '● 사용중' : '○ 비어있음'}
              </div>
              {t.hasActiveSession && (
                <button onClick={() => setEndTarget(t.id)} className="text-sm text-red-500 hover:underline">세션 종료</button>
              )}
            </div>
          ))}
        </div>
      )}

      <ConfirmDialog
        isOpen={endTarget !== null}
        title="세션 종료"
        message="이 테이블의 세션을 종료하시겠습니까?"
        onConfirm={async () => { if (endTarget) { await completeSession(endTarget); setEndTarget(null); } }}
        onCancel={() => setEndTarget(null)}
      />
    </div>
  );
}
