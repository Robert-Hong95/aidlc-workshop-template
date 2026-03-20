import { useQuery } from '@tanstack/react-query';
import { getOrderHistory } from '@table-order/api-client';
import { apiClient } from '../lib/api';

export function useOrderHistory(tableId: number | null, dateFrom: string, dateTo: string) {
  const { data: history = [], isLoading } = useQuery({
    queryKey: ['orderHistory', tableId, dateFrom, dateTo],
    queryFn: () => getOrderHistory(apiClient, tableId!, { dateFrom, dateTo }),
    enabled: !!tableId && !!dateFrom && !!dateTo,
  });

  return { history, isLoading };
}
