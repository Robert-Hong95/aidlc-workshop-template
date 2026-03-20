import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { getMenus, createMenu, updateMenu, deleteMenu, reorderMenus } from '@table-order/api-client';
import { apiClient } from '../lib/api';
import { useAdminAuthStore } from '../stores/auth-store';
import type { CreateMenuRequest, UpdateMenuRequest } from '@table-order/api-client';

export function useMenus() {
  const qc = useQueryClient();
  const storeId = useAdminAuthStore((s) => s.storeId);

  const { data: menus = [], isLoading } = useQuery({
    queryKey: ['menus', storeId],
    queryFn: () => getMenus(apiClient, storeId!),
    enabled: !!storeId,
  });

  const createMut = useMutation({
    mutationFn: (data: CreateMenuRequest) => createMenu(apiClient, storeId!, data),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['menus', storeId] }),
  });

  const updateMut = useMutation({
    mutationFn: ({ id, data }: { id: number; data: UpdateMenuRequest }) => updateMenu(apiClient, id, data),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['menus', storeId] }),
  });

  const deleteMut = useMutation({
    mutationFn: (id: number) => deleteMenu(apiClient, id),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['menus', storeId] }),
  });

  const reorderMut = useMutation({
    mutationFn: (ids: number[]) => reorderMenus(apiClient, storeId!, { ids }),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['menus', storeId] }),
  });

  return {
    menus, isLoading,
    createMenu: createMut.mutateAsync,
    updateMenu: updateMut.mutateAsync,
    deleteMenu: deleteMut.mutateAsync,
    reorderMenus: reorderMut.mutateAsync,
  };
}
