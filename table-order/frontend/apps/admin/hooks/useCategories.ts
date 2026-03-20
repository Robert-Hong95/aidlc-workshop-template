import { useState, useCallback } from 'react';
import type { Category, CreateCategoryRequest } from '@table-order/api-client';

export function useCategories() {
  const [categories, setCategories] = useState<Category[]>([]);
  const [isLoading, setIsLoading] = useState(false);

  const fetchCategories = useCallback(async (_storeId: number) => {
    setIsLoading(true);
    try {
      // API deferred
    } finally {
      setIsLoading(false);
    }
  }, []);

  const createCategory = useCallback(async (_data: CreateCategoryRequest) => {
    // API deferred
  }, []);

  const updateCategory = useCallback(async (_id: number, _name: string) => {
    // API deferred
  }, []);

  const deleteCategory = useCallback(async (_id: number) => {
    // API deferred
  }, []);

  const reorderCategories = useCallback(async (_ids: number[]) => {
    // API deferred
  }, []);

  return { categories, isLoading, fetchCategories, createCategory, updateCategory, deleteCategory, reorderCategories };
}
