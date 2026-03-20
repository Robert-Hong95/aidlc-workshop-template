import { useState } from 'react';
import { Button } from '@table-order/ui';
import { Input } from '@table-order/ui';

export function SetupForm({ onSubmit, isLoading, error }: {
  onSubmit: (data: { storeCode: string; tableNo: number; password: string }) => void;
  isLoading: boolean;
  error?: string;
}) {
  const [storeCode, setStoreCode] = useState('');
  const [tableNo, setTableNo] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit({ storeCode, tableNo: Number(tableNo), password });
  };

  return (
    <form data-testid="setup-form" onSubmit={handleSubmit} className="flex flex-col gap-4">
      <Input label="매장코드" value={storeCode} onChange={(e) => setStoreCode(e.target.value)} placeholder="매장코드 입력" data-testid="input-store-code" />
      <Input label="테이블번호" type="number" value={tableNo} onChange={(e) => setTableNo(e.target.value)} placeholder="테이블번호 입력" data-testid="input-table-no" />
      <Input label="비밀번호" type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="비밀번호 입력" data-testid="input-password" />
      {error && <p className="text-sm text-red-500">{error}</p>}
      <Button type="submit" loading={isLoading} disabled={isLoading} data-testid="submit-btn">연결하기</Button>
    </form>
  );
}
