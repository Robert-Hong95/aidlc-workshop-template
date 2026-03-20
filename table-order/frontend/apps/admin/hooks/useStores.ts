import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { getStores, createStore, updateStore, deleteStore } from '@table-order/api-client';
import { apiClient } from '../lib/api';

export function useStores() {
  const qc = useQueryClient();
  const { data: stores = [], isLoading } = useQuery({ queryKey: ['stores'], queryFn: () => getStores(apiClient) });

  const createMutation = useMutation({
    mutationFn: (data: { storeCode: string; name: string }) => createStore(apiClient, data),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['stores'] }),
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: number; data: { storeCode?: string; name?: string } }) => updateStore(apiClient, id, data),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['stores'] }),
  });

  const deleteMutation = useMutation({
    mutationFn: (id: number) => deleteStore(apiClient, id),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['stores'] }),
  });

  return { stores, isLoading, createStore: createMutation.mutateAsync, updateStore: updateMutation.mutateAsync, deleteStore: deleteMutation.mutateAsync };
}
