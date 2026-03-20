'use client';

import { formatPrice } from '@table-order/shared';
import { OrderPreview, type DashboardOrder } from './OrderPreview';

export interface TableCardData {
  tableId: number;
  tableNo: number;
  totalAmount: number;
  orders: DashboardOrder[];
  hasNewOrder?: boolean;
}

export function TableCard({ data, onChangeStatus, onClick }: {
  data: TableCardData;
  onChangeStatus: (orderId: number, status: string) => void;
  onClick: () => void;
}) {
  return (
    <div
      className={`border rounded-lg p-4 bg-white cursor-pointer hover:shadow-md transition-shadow ${data.hasNewOrder ? 'ring-2 ring-[#FF9900] animate-pulse' : ''}`}
      data-testid={`table-card-${data.tableNo}`}
      onClick={onClick}
    >
      <div className="flex justify-between items-center mb-2">
        <span className="font-bold text-[#232F3E]">테이블 {data.tableNo}</span>
        <span className="text-sm font-semibold text-[#FF9900]">{formatPrice(data.totalAmount)}</span>
      </div>
      {data.orders.length === 0 ? (
        <p className="text-xs text-gray-400">주문 없음</p>
      ) : (
        data.orders.slice(0, 2).map((order) => (
          <OrderPreview key={order.id} order={order} onChangeStatus={onChangeStatus} />
        ))
      )}
      {data.orders.length > 2 && <p className="text-xs text-gray-400 mt-2 text-center">외 {data.orders.length - 2}건</p>}
    </div>
  );
}
