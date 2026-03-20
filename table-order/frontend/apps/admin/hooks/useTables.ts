import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { getTables, setupTable, endSession } from '@table-order/api-client';
import { apiClient } from '../lib/api';
import { useAdminAuthStore } from '../stores/auth-store';

export function useTables() {
  const qc = useQueryClient();
  const storeId = useAdminAuthStore((s) => s.storeId);

  const { data: tables = [], isLoading } = useQuery({
    queryKey: ['tables', storeId],
    queryFn: () => getTables(apiClient, storeId!),
    enabled: !!storeId,
  });

  const setupMutation = useMutation({
    mutationFn: (data: { tableNo: number; password: string }) => setupTable(apiClient, storeId!, data),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['tables', storeId] }),
  });

  const endSessionMutation = useMutation({
    mutationFn: (tableId: number) => endSession(apiClient, tableId),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['tables', storeId] }),
  });

  return { tables, isLoading, setupTable: setupMutation.mutateAsync, endSession: endSessionMutation.mutateAsync };
}
