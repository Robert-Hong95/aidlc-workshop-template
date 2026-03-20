import { describe, it, expect, beforeEach, vi, afterEach } from 'vitest';
import { renderHook, act } from '@testing-library/react';
import { useLoginLockout } from '../../hooks/useLoginLockout';

describe('useLoginLockout', () => {
  beforeEach(() => { vi.useFakeTimers(); });
  afterEach(() => { vi.useRealTimers(); });

  it('TC-5FE-017: initial state has 0 attempts and not locked', () => {
    const { result } = renderHook(() => useLoginLockout());
    expect(result.current.attempts).toBe(0);
    expect(result.current.isLocked).toBe(false);
  });

  it('TC-5FE-018: recordFailure increments attempts', () => {
    const { result } = renderHook(() => useLoginLockout());
    act(() => result.current.recordFailure());
    expect(result.current.attempts).toBe(1);
    expect(result.current.isLocked).toBe(false);
  });

  it('TC-5FE-019: 6th failure triggers 1 minute lockout', () => {
    const { result } = renderHook(() => useLoginLockout());
    for (let i = 0; i < 6; i++) act(() => result.current.recordFailure());
    expect(result.current.attempts).toBe(6);
    expect(result.current.isLocked).toBe(true);
    expect(result.current.remainingSeconds).toBe(60);
  });

  it('TC-5FE-020: resetAttempts clears lockout', () => {
    const { result } = renderHook(() => useLoginLockout());
    for (let i = 0; i < 6; i++) act(() => result.current.recordFailure());
    act(() => result.current.resetAttempts());
    expect(result.current.attempts).toBe(0);
    expect(result.current.isLocked).toBe(false);
  });
});
