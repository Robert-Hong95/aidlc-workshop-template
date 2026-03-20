import { useQuery } from '@tanstack/react-query';
import { getCustomerMenus } from '@table-order/api-client';
import { apiClient } from '../lib/api';
import { useTableAuthStore } from '../stores/auth-store';

export function useCustomerMenu() {
  const storeId = useTableAuthStore((s) => s.storeId);

  const { data: menuData, isLoading } = useQuery({
    queryKey: ['customerMenus', storeId],
    queryFn: () => getCustomerMenus(apiClient, storeId!),
    enabled: !!storeId,
  });

  return { menuData, isLoading };
}
