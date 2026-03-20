import { useState, useCallback } from 'react';
import type { Category, CreateCategoryRequest } from '@table-order/api-client';
import { useAdminAuthStore } from '../stores/auth-store';

const API = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

function authHeaders() {
  const token = useAdminAuthStore.getState().accessToken;
  return { 'Content-Type': 'application/json', ...(token ? { Authorization: `Bearer ${token}` } : {}) };
}

export function useCategories() {
  const [categories, setCategories] = useState<Category[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [storeIdRef, setStoreIdRef] = useState<number | null>(null);

  const fetchCategories = useCallback(async (storeId: number) => {
    setStoreIdRef(storeId);
    setIsLoading(true);
    try {
      const res = await fetch(`${API}/api/admin/stores/${storeId}/categories`, { headers: authHeaders() });
      const json = await res.json();
      if (json.success) setCategories(json.data || []);
    } finally { setIsLoading(false); }
  }, []);

  const createCategory = useCallback(async (data: CreateCategoryRequest) => {
    if (!storeIdRef) return;
    await fetch(`${API}/api/admin/stores/${storeIdRef}/categories`, {
      method: 'POST', headers: authHeaders(), body: JSON.stringify(data),
    });
    await fetchCategories(storeIdRef);
  }, [storeIdRef, fetchCategories]);

  const updateCategory = useCallback(async (id: number, name: string) => {
    await fetch(`${API}/api/admin/categories/${id}`, {
      method: 'PUT', headers: authHeaders(), body: JSON.stringify({ name }),
    });
    if (storeIdRef) await fetchCategories(storeIdRef);
  }, [storeIdRef, fetchCategories]);

  const deleteCategory = useCallback(async (id: number) => {
    await fetch(`${API}/api/admin/categories/${id}`, { method: 'DELETE', headers: authHeaders() });
    if (storeIdRef) await fetchCategories(storeIdRef);
  }, [storeIdRef, fetchCategories]);

  return { categories, isLoading, fetchCategories, createCategory, updateCategory, deleteCategory };
}
