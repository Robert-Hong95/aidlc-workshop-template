'use client';

import { useState } from 'react';
import { AdminHeader } from './AdminHeader';
import { Sidebar } from './Sidebar';

export function AdminLayout({ children }: { children: React.ReactNode }) {
  const [sidebarCollapsed, setSidebarCollapsed] = useState(false);
  const [mobileOpen, setMobileOpen] = useState(false);

  return (
    <div data-testid="admin-layout" className="h-screen flex flex-col bg-[#F2F3F3]">
      <AdminHeader
        storeName=""
        onMenuToggle={() => setMobileOpen(!mobileOpen)}
        onLogout={() => {}}
      />
      <div className="flex flex-1 overflow-hidden">
        <div className={`hidden lg:block ${sidebarCollapsed ? 'w-16' : 'w-56'}`}>
          <Sidebar collapsed={sidebarCollapsed} onToggle={() => setSidebarCollapsed(!sidebarCollapsed)} currentPath="" />
        </div>
        {mobileOpen && (
          <div className="lg:hidden fixed inset-0 z-40 flex">
            <div className="w-56"><Sidebar collapsed={false} onToggle={() => setMobileOpen(false)} currentPath="" /></div>
            <div className="flex-1 bg-black/50" onClick={() => setMobileOpen(false)} />
          </div>
        )}
        <main className="flex-1 overflow-auto p-6">{children}</main>
      </div>
    </div>
  );
}
