export function DatePresets({ onSelect }: { onSelect: (range: { from: string; to: string }) => void }) {
  const today = () => {
    const d = new Date().toISOString().split('T')[0];
    return { from: d, to: d };
  };
  const daysAgo = (n: number) => {
    const to = new Date().toISOString().split('T')[0];
    const from = new Date(Date.now() - n * 86400000).toISOString().split('T')[0];
    return { from, to };
  };

  return (
    <div className="flex gap-2" data-testid="date-presets">
      <button onClick={() => onSelect(today())} className="px-3 py-1 text-sm border rounded-md hover:bg-gray-100">오늘</button>
      <button onClick={() => onSelect(daysAgo(7))} className="px-3 py-1 text-sm border rounded-md hover:bg-gray-100">최근 7일</button>
      <button onClick={() => onSelect(daysAgo(30))} className="px-3 py-1 text-sm border rounded-md hover:bg-gray-100">최근 30일</button>
    </div>
  );
}
