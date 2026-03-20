'use client';

import { useEffect, useState } from 'react';
import { Modal, Button } from '@table-order/ui';

export function OrderSuccessModal({ isOpen, orderNo, onClose }: {
  isOpen: boolean;
  orderNo: string;
  onClose: () => void;
}) {
  const [countdown, setCountdown] = useState(5);

  useEffect(() => {
    if (!isOpen) { setCountdown(5); return; }
    const timer = setInterval(() => {
      setCountdown((prev) => {
        if (prev <= 1) { clearInterval(timer); onClose(); return 0; }
        return prev - 1;
      });
    }, 1000);
    return () => clearInterval(timer);
  }, [isOpen, onClose]);

  return (
    <Modal isOpen={isOpen} onClose={onClose} data-testid="order-success-modal">
      <div className="text-center py-4">
        <p className="text-4xl mb-4">✅</p>
        <h3 className="text-lg font-bold text-[#232F3E] mb-2">주문이 완료되었습니다!</h3>
        <p className="text-sm text-gray-600 mb-1">주문번호: <span className="font-semibold">{orderNo}</span></p>
        <p className="text-xs text-gray-400">{countdown}초 후 메뉴 화면으로 이동합니다</p>
        <Button className="mt-4" variant="secondary" onClick={onClose}>바로 이동</Button>
      </div>
    </Modal>
  );
}
