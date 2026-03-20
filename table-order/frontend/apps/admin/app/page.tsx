'use client';

import { useEffect, useState } from 'react';
import { useAdminAuthStore } from '../stores/auth-store';
import { useAdminAuth } from '../hooks/useAdminAuth';
import { Button, Input } from '@table-order/ui';
import { redirect } from 'next/navigation';

export default function RootPage() {
  const isAuthenticated = useAdminAuthStore((s) => s.isAuthenticated);
  const { login, isLoading, error } = useAdminAuth();
  const [hydrated, setHydrated] = useState(false);
  const [storeCode, setStoreCode] = useState('');
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  useEffect(() => {
    useAdminAuthStore.getState().hydrate();
    setHydrated(true);
  }, []);

  if (!hydrated) return null;
  if (isAuthenticated) redirect('/dashboard');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    login(storeCode, username, password);
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 p-4">
      <div className="w-full max-w-sm bg-white rounded-lg shadow p-6">
        <h1 className="text-xl font-bold text-[#232F3E] mb-2 text-center">테이블오더 관리자</h1>
        <p className="text-sm text-gray-500 mb-6 text-center">매장 관리 시스템 로그인</p>
        <form onSubmit={handleSubmit} className="flex flex-col gap-4">
          <Input label="매장코드" value={storeCode} onChange={(e) => setStoreCode(e.target.value)} placeholder="STORE01" />
          <Input label="아이디" value={username} onChange={(e) => setUsername(e.target.value)} placeholder="admin" />
          <Input label="비밀번호" type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="비밀번호" />
          {error && <p className="text-sm text-red-500">{error}</p>}
          <Button type="submit" loading={isLoading} disabled={isLoading}>로그인</Button>
        </form>
      </div>
    </div>
  );
}
