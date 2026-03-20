import { useState, useCallback } from 'react';
import type { OrderHistoryItem, OrderHistoryFilter } from '@table-order/api-client';

export function useOrderHistory(_tableId: number) {
  const today = new Date().toISOString().split('T')[0];
  const [history] = useState<OrderHistoryItem[]>([]);
  const [isLoading] = useState(false);
  const [filter, setFilter] = useState<OrderHistoryFilter>({ from: today, to: today });

  return { history, isLoading, filter, setFilter };
}
