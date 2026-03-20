import { useState, useCallback, useRef, useEffect } from 'react';

const LOCKOUT_DELAYS: Record<number, number> = { 6: 60, 7: 300, 8: 900 }; // 9+: 3600

export function useLoginLockout() {
  const [attempts, setAttempts] = useState(0);
  const [remainingSeconds, setRemainingSeconds] = useState(0);
  const timerRef = useRef<ReturnType<typeof setInterval> | null>(null);

  const isLocked = remainingSeconds > 0;

  const clearTimer = () => {
    if (timerRef.current) { clearInterval(timerRef.current); timerRef.current = null; }
  };

  const startCountdown = (seconds: number) => {
    setRemainingSeconds(seconds);
    clearTimer();
    timerRef.current = setInterval(() => {
      setRemainingSeconds((prev) => {
        if (prev <= 1) { clearTimer(); return 0; }
        return prev - 1;
      });
    }, 1000);
  };

  const recordFailure = useCallback(() => {
    setAttempts((prev) => {
      const next = prev + 1;
      const delay = LOCKOUT_DELAYS[next] ?? (next >= 9 ? 3600 : 0);
      if (delay > 0) startCountdown(delay);
      return next;
    });
  }, []);

  const resetAttempts = useCallback(() => {
    setAttempts(0);
    setRemainingSeconds(0);
    clearTimer();
  }, []);

  useEffect(() => clearTimer, []);

  return { attempts, isLocked, remainingSeconds, recordFailure, resetAttempts };
}
