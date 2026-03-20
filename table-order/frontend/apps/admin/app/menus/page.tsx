'use client';

import { useState, useEffect } from 'react';
import { Button, ConfirmDialog, Spinner } from '@table-order/ui';
import { AdminLayout } from '../../components/layout/AdminLayout';
import { CategoryManager } from '../../components/features/menu/CategoryManager';
import { MenuList } from '../../components/features/menu/MenuList';
import { MenuForm } from '../../components/features/menu/MenuForm';
import { ImageUpload } from '../../components/features/menu/ImageUpload';
import { useMenus } from '../../hooks/useMenus';
import { useCategories } from '../../hooks/useCategories';
import { useAdminAuthStore } from '../../stores/auth-store';
import { redirect } from 'next/navigation';
import type { Menu } from '@table-order/api-client';

export default function MenuManagePage() {
  const { categories, fetchCategories, createCategory, updateCategory, deleteCategory } = useCategories();
  const { menus, isLoading, fetchMenus, createMenu, updateMenu, deleteMenu } = useMenus();
  const storeId = useAdminAuthStore((s) => s.storeId);
  const isAuthenticated = useAdminAuthStore((s) => s.isAuthenticated);
  const [selectedCategoryId, setSelectedCategoryId] = useState<number | null>(null);
  const [editingMenu, setEditingMenu] = useState<Menu | null>(null);
  const [showForm, setShowForm] = useState(false);
  const [deleteTarget, setDeleteTarget] = useState<number | null>(null);
  const [imageFile, setImageFile] = useState<File | null>(null);
  const [hydrated, setHydrated] = useState(false);

  useEffect(() => {
    useAdminAuthStore.getState().hydrate();
    setHydrated(true);
  }, []);

  useEffect(() => {
    if (storeId) {
      fetchMenus(storeId);
      fetchCategories(storeId);
    }
  }, [storeId, fetchMenus, fetchCategories]);

  if (!hydrated) return <div className="flex items-center justify-center min-h-screen"><Spinner size="lg" /></div>;
  if (!isAuthenticated) redirect('/login');

  const filteredMenus = selectedCategoryId ? menus.filter((m) => m.categoryId === selectedCategoryId) : menus;

  const uploadImage = async (file: File): Promise<string | undefined> => {
    const formData = new FormData();
    formData.append('file', file);
    const API = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';
    const res = await fetch(`${API}/api/files/upload`, { method: 'POST', body: formData });
    const json = await res.json();
    return json.success ? `${API}/api/files/${json.data}` : undefined;
  };

  const handleSubmit = async (data: { name: string; price: number; categoryId: number; description?: string; imageUrl?: string }) => {
    let imageUrl = data.imageUrl;
    if (imageFile) {
      imageUrl = await uploadImage(imageFile) ?? imageUrl;
    }
    if (editingMenu) {
      await updateMenu(editingMenu.id, { ...data, imageUrl });
    } else {
      await createMenu({ ...data, imageUrl });
    }
    setShowForm(false);
    setEditingMenu(null);
    setImageFile(null);
  };

  const handleEdit = (menu: Menu) => {
    setEditingMenu(menu);
    setShowForm(true);
  };

  const handleDelete = async () => {
    if (deleteTarget !== null) {
      await deleteMenu(deleteTarget);
      setDeleteTarget(null);
    }
  };

  return (
    <AdminLayout>
      <div className="max-w-4xl mx-auto" data-testid="menu-manage-page">
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-xl font-bold text-[#232F3E]">메뉴 관리</h1>
          <Button onClick={() => { setEditingMenu(null); setShowForm(true); }} data-testid="add-menu-btn">메뉴 추가</Button>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
          <div className="md:col-span-1">
            <CategoryManager
              categories={categories}
              onAdd={() => { const name = prompt('카테고리 이름'); if (name) createCategory({ name }); }}
              onEdit={(cat) => { const name = prompt('카테고리 이름', cat.name); if (name) updateCategory(cat.id, name); }}
              onDelete={(id) => deleteCategory(id)}
            />
            <ul className="mt-2 flex flex-col gap-1">
              <li>
                <button
                  onClick={() => setSelectedCategoryId(null)}
                  className={`w-full text-left px-3 py-2 rounded text-sm ${!selectedCategoryId ? 'bg-[#FF9900] text-white' : 'hover:bg-gray-100'}`}
                >
                  전체
                </button>
              </li>
              {categories.map((cat) => (
                <li key={cat.id}>
                  <button
                    onClick={() => setSelectedCategoryId(cat.id)}
                    className={`w-full text-left px-3 py-2 rounded text-sm ${selectedCategoryId === cat.id ? 'bg-[#FF9900] text-white' : 'hover:bg-gray-100'}`}
                  >
                    {cat.name}
                  </button>
                </li>
              ))}
            </ul>
          </div>

          <div className="md:col-span-3">
            {showForm ? (
              <div className="border rounded-lg p-4">
                <h2 className="font-semibold mb-4">{editingMenu ? '메뉴 수정' : '메뉴 등록'}</h2>
                <ImageUpload value={editingMenu?.imageUrl} onChange={(file) => setImageFile(file)} />
                <MenuForm
                  categories={categories}
                  menu={editingMenu ?? undefined}
                  onSubmit={handleSubmit}
                  isLoading={isLoading}
                  onCancel={() => { setShowForm(false); setEditingMenu(null); }}
                />
              </div>
            ) : (
              <MenuList menus={filteredMenus} onEdit={handleEdit} onDelete={(id) => setDeleteTarget(id)} />
            )}
          </div>
        </div>

        <ConfirmDialog
          isOpen={deleteTarget !== null}
          title="메뉴 삭제"
          message="이 메뉴를 삭제하시겠습니까?"
          onConfirm={handleDelete}
          onCancel={() => setDeleteTarget(null)}
        />
      </div>
    </AdminLayout>
  );
}
