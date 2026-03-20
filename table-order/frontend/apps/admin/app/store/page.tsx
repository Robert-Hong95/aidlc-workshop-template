'use client';

import { useEffect } from 'react';
import { Spinner } from '@table-order/ui';
import { useStores } from '../../hooks/useStores';
import { useAdminAuthStore } from '../../stores/auth-store';

export default function StorePage() {
  const storeId = useAdminAuthStore((s) => s.storeId);
  const storeName = useAdminAuthStore((s) => s.storeName);
  const { stores, isLoading, fetchStores, updateStore } = useStores();

  useEffect(() => {
    useAdminAuthStore.getState().hydrate();
  }, []);

  useEffect(() => {
    if (storeId) fetchStores();
  }, [storeId, fetchStores]);

  if (isLoading) {
    return <div className="flex items-center justify-center min-h-[400px]"><Spinner size="lg" /></div>;
  }

  const currentStore = stores.find((s) => s.id === storeId);

  return (
    <div data-testid="store-page">
      <h1 className="text-xl font-bold text-[#232F3E] mb-4">매장 정보</h1>
      <div className="border rounded-lg p-6 bg-white max-w-lg">
        <div className="mb-4">
          <span className="text-sm text-gray-500">매장명</span>
          <p className="text-lg font-semibold">{storeName}</p>
        </div>
        {currentStore && (
          <>
            <div className="mb-4">
              <span className="text-sm text-gray-500">매장코드</span>
              <p className="text-lg font-semibold">{currentStore.storeCode}</p>
            </div>
            <div>
              <span className="text-sm text-gray-500">생성일</span>
              <p className="text-sm">{new Date(currentStore.createdAt).toLocaleDateString('ko-KR')}</p>
            </div>
          </>
        )}
      </div>
    </div>
  );
}
