// Hooks — API 연동 deferred, 타입 정의만
// 실제 React Query 연동은 Backend 완성 후

import { useState, useCallback } from 'react';
import type { Store } from '@table-order/api-client';

export function useStores() {
  const [stores] = useState<Store[]>([]);
  const [isLoading] = useState(false);

  const createStore = useCallback(async (_data: { name: string; address?: string; phone?: string }) => {
    // API deferred
  }, []);

  const updateStore = useCallback(async (_id: number, _data: { name?: string; address?: string; phone?: string }) => {
    // API deferred
  }, []);

  return { stores, isLoading, createStore, updateStore };
}
