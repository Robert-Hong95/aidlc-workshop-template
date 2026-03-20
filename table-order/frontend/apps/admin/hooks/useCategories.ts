import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { getCategories, createCategory, updateCategory, deleteCategory, reorderCategories } from '@table-order/api-client';
import { apiClient } from '../lib/api';
import { useAdminAuthStore } from '../stores/auth-store';

export function useCategories() {
  const qc = useQueryClient();
  const storeId = useAdminAuthStore((s) => s.storeId);

  const { data: categories = [], isLoading } = useQuery({
    queryKey: ['categories', storeId],
    queryFn: () => getCategories(apiClient, storeId!),
    enabled: !!storeId,
  });

  const createMut = useMutation({
    mutationFn: (data: { name: string }) => createCategory(apiClient, storeId!, data),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['categories', storeId] }),
  });

  const updateMut = useMutation({
    mutationFn: ({ id, name }: { id: number; name: string }) => updateCategory(apiClient, id, { name }),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['categories', storeId] }),
  });

  const deleteMut = useMutation({
    mutationFn: (id: number) => deleteCategory(apiClient, id),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['categories', storeId] }),
  });

  const reorderMut = useMutation({
    mutationFn: (ids: number[]) => reorderCategories(apiClient, storeId!, { ids }),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['categories', storeId] }),
  });

  return {
    categories, isLoading,
    createCategory: createMut.mutateAsync,
    updateCategory: updateMut.mutateAsync,
    deleteCategory: deleteMut.mutateAsync,
    reorderCategories: reorderMut.mutateAsync,
  };
}
