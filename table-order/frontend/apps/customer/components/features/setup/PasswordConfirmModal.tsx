import { useState } from 'react';
import { Button, Input, Modal } from '@table-order/ui';

export function PasswordConfirmModal({ isOpen, onConfirm, onCancel, isLoading, error }: {
  isOpen: boolean;
  onConfirm: (password: string) => void;
  onCancel: () => void;
  isLoading: boolean;
  error?: string;
}) {
  const [password, setPassword] = useState('');

  if (!isOpen) return null;

  return (
    <div data-testid="password-confirm-modal">
      <Modal isOpen={isOpen} onClose={onCancel} title="비밀번호 확인">
        <div className="flex flex-col gap-4">
          <Input type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="비밀번호 입력" data-testid="modal-password-input" />
          {error && <p className="text-sm text-red-500">{error}</p>}
          <div className="flex gap-3 justify-end">
            <Button variant="secondary" onClick={onCancel} data-testid="modal-cancel">취소</Button>
            <Button loading={isLoading} onClick={() => onConfirm(password)} data-testid="modal-confirm">확인</Button>
          </div>
        </div>
      </Modal>
    </div>
  );
}
