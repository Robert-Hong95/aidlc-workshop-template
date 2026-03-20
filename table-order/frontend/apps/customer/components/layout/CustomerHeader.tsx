export function CustomerHeader({ storeName, tableNo, onSettingsClick }: {
  storeName: string; tableNo: number; onSettingsClick: () => void;
}) {
  return (
    <header data-testid="customer-header" className="h-14 bg-white border-b flex items-center justify-between px-4">
      <div className="flex items-center gap-2">
        <span className="font-semibold text-[#232F3E]">{storeName}</span>
        <span className="bg-[#FF9900] text-white text-xs px-2 py-0.5 rounded-full">{tableNo}</span>
      </div>
      <button data-testid="settings-btn" onClick={onSettingsClick} className="text-[#232F3E] text-xl">⚙️</button>
    </header>
  );
}
