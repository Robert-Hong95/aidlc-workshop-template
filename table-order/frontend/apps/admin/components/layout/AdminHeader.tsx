export function AdminHeader({ storeName, onMenuToggle, onLogout }: {
  storeName: string; onMenuToggle: () => void; onLogout: () => void;
}) {
  return (
    <header data-testid="admin-header" className="h-14 bg-[#232F3E] text-white flex items-center justify-between px-4">
      <div className="flex items-center gap-3">
        <button data-testid="menu-toggle" onClick={onMenuToggle} className="lg:hidden p-1">☰</button>
        <span className="font-semibold">{storeName}</span>
      </div>
      <button data-testid="logout-btn" onClick={onLogout} className="text-sm hover:text-[#FF9900]">로그아웃</button>
    </header>
  );
}
