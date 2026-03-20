const tabs = [
  { path: '/menu', label: '메뉴', icon: '📋', testId: 'nav-menu' },
  { path: '/cart', label: '장바구니', icon: '🛒', testId: 'nav-cart' },
  { path: '/orders', label: '주문내역', icon: '📜', testId: 'nav-orders' },
];

export function BottomNav({ currentPath, cartItemCount }: {
  currentPath: string; cartItemCount?: number;
}) {
  return (
    <nav data-testid="bottom-nav" className="h-16 bg-white border-t flex items-center justify-around">
      {tabs.map((tab) => (
        <a
          key={tab.path}
          href={tab.path}
          data-testid={tab.testId}
          className={`flex flex-col items-center gap-0.5 text-xs relative ${
            currentPath === tab.path ? 'text-[#FF9900] active' : 'text-gray-500'
          }`}
        >
          <span className="text-lg">{tab.icon}</span>
          <span>{tab.label}</span>
          {tab.path === '/cart' && cartItemCount && cartItemCount > 0 ? (
            <span data-testid="cart-badge" className="absolute -top-1 -right-2 bg-[#FF9900] text-white text-[10px] w-4 h-4 rounded-full flex items-center justify-center">
              {cartItemCount}
            </span>
          ) : null}
        </a>
      ))}
    </nav>
  );
}
