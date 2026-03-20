const navItems = [
  { path: '/dashboard', label: '대시보드', icon: '📊', testId: 'nav-dashboard' },
  { path: '/tables', label: '테이블', icon: '🪑', testId: 'nav-tables' },
  { path: '/menus', label: '메뉴', icon: '📋', testId: 'nav-menus' },
  { path: '/store', label: '매장', icon: '🏪', testId: 'nav-store' },
];

export function Sidebar({ collapsed, onToggle, currentPath }: {
  collapsed: boolean; onToggle: () => void; currentPath: string;
}) {
  return (
    <nav data-testid="sidebar" className={`bg-[#232F3E] text-white h-full transition-all ${collapsed ? 'w-16' : 'w-56'}`}>
      <div className="p-3 flex justify-end">
        <button data-testid="sidebar-toggle" onClick={onToggle} className="text-gray-400 hover:text-white">
          {collapsed ? '→' : '←'}
        </button>
      </div>
      <ul className="flex flex-col gap-1 px-2">
        {navItems.map((item) => (
          <li key={item.path}>
            <a
              href={item.path}
              data-testid={item.testId}
              className={`flex items-center gap-3 px-3 py-2 rounded-md text-sm transition-colors ${
                currentPath === item.path ? 'bg-[#FF9900] text-white active' : 'text-gray-300 hover:bg-white/10'
              }`}
            >
              <span>{item.icon}</span>
              {!collapsed && <span>{item.label}</span>}
            </a>
          </li>
        ))}
      </ul>
    </nav>
  );
}
