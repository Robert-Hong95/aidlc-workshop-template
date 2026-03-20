import { useState, useCallback } from 'react';
import { useAdminAuthStore } from '../stores/auth-store';
import { categoryApi, type Category, type CreateCategoryRequest } from '@table-order/api-client';

export function useCategories() {
  const [categories, setCategories] = useState<Category[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const token = useAdminAuthStore((s) => s.accessToken);
  const storeId = useAdminAuthStore((s) => s.storeId);

  const fetchCategories = useCallback(async (_storeId?: number) => {
    const sid = _storeId ?? storeId;
    if (!token || !sid) return;
    setIsLoading(true);
    try { setCategories(await categoryApi.list(token, sid)); } finally { setIsLoading(false); }
  }, [token, storeId]);

  const createCategory = useCallback(async (data: CreateCategoryRequest) => {
    if (!token || !storeId) return;
    await categoryApi.create(token, storeId, data);
    await fetchCategories();
  }, [token, storeId, fetchCategories]);

  const updateCategory = useCallback(async (id: number, name: string) => {
    if (!token) return;
    await categoryApi.update(token, id, { name });
    await fetchCategories();
  }, [token, fetchCategories]);

  const deleteCategory = useCallback(async (id: number) => {
    if (!token) return;
    await categoryApi.delete(token, id);
    await fetchCategories();
  }, [token, fetchCategories]);

  const reorderCategories = useCallback(async (ids: number[]) => {
    if (!token || !storeId) return;
    await categoryApi.reorder(token, storeId, ids);
    await fetchCategories();
  }, [token, storeId, fetchCategories]);

  return { categories, isLoading, fetchCategories, createCategory, updateCategory, deleteCategory, reorderCategories };
}
