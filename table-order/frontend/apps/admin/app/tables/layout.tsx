'use client';

import { useEffect, useState } from 'react';
import { useAdminAuthStore } from '../../stores/auth-store';
import { AdminLayout } from '../../components/layout/AdminLayout';

export default function TablesLayout({ children }: { children: React.ReactNode }) {
  const [hydrated, setHydrated] = useState(false);
  const isAuthenticated = useAdminAuthStore((s) => s.isAuthenticated);

  useEffect(() => {
    useAdminAuthStore.getState().hydrate();
    setHydrated(true);
  }, []);

  if (!hydrated) return null;

  if (!isAuthenticated) {
    if (typeof window !== 'undefined') window.location.href = '/';
    return null;
  }

  return <AdminLayout>{children}</AdminLayout>;
}
