'use client';

import { useState } from 'react';
import { Button, Input } from '@table-order/ui';
import { useAdminAuth } from '../../hooks/useAdminAuth';
import { redirect } from 'next/navigation';
import { useAdminAuthStore } from '../../stores/auth-store';

export default function LoginPage() {
  const { login, isAuthenticated, isLoading, error, lockoutRemaining } = useAdminAuth();
  const [storeCode, setStoreCode] = useState('');
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  if (isAuthenticated) redirect('/dashboard');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    login(storeCode, username, password);
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-[#F2F3F3] p-4">
      <div className="w-full max-w-sm bg-white rounded-lg shadow p-6">
        <h1 className="text-xl font-bold text-[#232F3E] text-center mb-6">관리자 로그인</h1>
        <form onSubmit={handleSubmit} className="flex flex-col gap-4" data-testid="login-form">
          <Input label="매장코드" value={storeCode} onChange={(e) => setStoreCode(e.target.value)} placeholder="매장코드 입력" />
          <Input label="아이디" value={username} onChange={(e) => setUsername(e.target.value)} placeholder="아이디 입력" />
          <Input label="비밀번호" type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="비밀번호 입력" />
          {error && <p className="text-sm text-red-500">{error}</p>}
          {lockoutRemaining > 0 && <p className="text-sm text-orange-500">{lockoutRemaining}초 후 다시 시도해주세요</p>}
          <Button type="submit" loading={isLoading} disabled={isLoading}>로그인</Button>
        </form>
      </div>
    </div>
  );
}
