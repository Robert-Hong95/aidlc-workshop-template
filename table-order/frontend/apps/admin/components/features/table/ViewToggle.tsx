export function ViewToggle({ mode, onChange }: { mode: 'list' | 'card'; onChange: (mode: 'list' | 'card') => void }) {
  return (
    <div className="flex gap-1 border rounded-md overflow-hidden" data-testid="view-toggle">
      <button data-testid="toggle-list" onClick={() => onChange('list')} className={`px-3 py-1 text-sm ${mode === 'list' ? 'bg-[#FF9900] text-white' : 'bg-white'}`}>☰</button>
      <button data-testid="toggle-card" onClick={() => onChange('card')} className={`px-3 py-1 text-sm ${mode === 'card' ? 'bg-[#FF9900] text-white' : 'bg-white'}`}>▦</button>
    </div>
  );
}
