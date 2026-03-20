import { Button } from '@table-order/ui';
import { formatPrice } from '@table-order/shared';

export function SessionCompleteDialog({ isOpen, tableName, totalAmount, onConfirm, onCancel }: {
  isOpen: boolean;
  tableName: string;
  totalAmount: number;
  onConfirm: () => void;
  onCancel: () => void;
}) {
  if (!isOpen) return null;
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50">
      <div className="bg-white rounded-lg p-6 max-w-sm w-full mx-4 shadow-xl" data-testid="session-complete-dialog">
        <h3 className="text-lg font-semibold text-[#232F3E] mb-2">이용 완료</h3>
        <p className="text-sm text-gray-600 mb-2">{tableName} 이용을 완료하시겠습니까?</p>
        <p className="text-lg font-bold text-[#232F3E] mb-6">총 주문 금액: {formatPrice(totalAmount)}</p>
        <div className="flex gap-3 justify-end">
          <Button variant="secondary" onClick={onCancel}>취소</Button>
          <Button variant="primary" onClick={onConfirm} data-testid="session-confirm-btn">확인</Button>
        </div>
      </div>
    </div>
  );
}
