'use client';

import { CustomerHeader } from './CustomerHeader';
import { BottomNav } from './BottomNav';

export function CustomerLayout({ children }: { children: React.ReactNode }) {
  return (
    <div data-testid="customer-layout" className="h-screen flex flex-col bg-white">
      <CustomerHeader storeName="" tableNo={0} onSettingsClick={() => {}} />
      <main className="flex-1 overflow-auto">{children}</main>
      <BottomNav currentPath="" />
    </div>
  );
}
