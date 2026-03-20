import { Modal, Spinner } from '@table-order/ui';
import { formatPrice, formatDateTime } from '@table-order/shared';
import { DatePresets } from './DatePresets';
import type { OrderHistoryItem, OrderHistoryFilter } from '@table-order/api-client';

export function OrderHistoryModal({ isOpen, tableId, onClose, history, isLoading, filter, onFilterChange }: {
  isOpen: boolean;
  tableId: number;
  onClose: () => void;
  history: OrderHistoryItem[];
  isLoading: boolean;
  filter: OrderHistoryFilter;
  onFilterChange: (filter: OrderHistoryFilter) => void;
}) {
  if (!isOpen) return null;
  return (
    <div data-testid="order-history-modal">
      <Modal isOpen={isOpen} onClose={onClose} title="과거 주문 내역" data-testid="history-modal">
        <div className="flex flex-col gap-4">
          <DatePresets onSelect={onFilterChange} />
          {isLoading ? <Spinner /> : (
            <div className="max-h-80 overflow-auto">
              {history.length === 0 ? <p className="text-sm text-gray-500 text-center py-4">내역이 없습니다</p> : (
                <table className="w-full text-sm">
                  <thead><tr className="border-b"><th className="py-1 text-left">주문번호</th><th className="py-1 text-left">시각</th><th className="py-1 text-right">금액</th></tr></thead>
                  <tbody>
                    {history.map((h) => (
                      <tr key={h.orderId} className="border-b">
                        <td className="py-1">{h.orderNo}</td>
                        <td className="py-1">{formatDateTime(h.orderedAt)}</td>
                        <td className="py-1 text-right">{formatPrice(h.totalAmount)}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              )}
            </div>
          )}
        </div>
      </Modal>
    </div>
  );
}
