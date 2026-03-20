import { describe, it, expect, beforeEach, vi } from 'vitest';
import { useAdminAuthStore } from '../../stores/auth-store';

const localStorageMock = (() => {
  let store: Record<string, string> = {};
  return {
    getItem: vi.fn((key: string) => store[key] ?? null),
    setItem: vi.fn((key: string, value: string) => { store[key] = value; }),
    removeItem: vi.fn((key: string) => { delete store[key]; }),
    clear: () => { store = {}; },
  };
})();

beforeEach(() => {
  localStorageMock.clear();
  vi.stubGlobal('localStorage', localStorageMock);
  useAdminAuthStore.setState({
    accessToken: null, refreshToken: null, storeId: null, storeName: null,
    adminId: null, username: null, isAuthenticated: false,
  });
});

describe('useAdminAuthStore', () => {
  it('TC-5FE-021: login sets auth state and persists to localStorage', () => {
    const response = { accessToken: 'at', refreshToken: 'rt', storeId: 1, storeName: 'Test Store', adminId: 1, username: 'admin' };
    useAdminAuthStore.getState().login(response);

    const state = useAdminAuthStore.getState();
    expect(state.isAuthenticated).toBe(true);
    expect(state.accessToken).toBe('at');
    expect(state.storeId).toBe(1);
    expect(localStorageMock.setItem).toHaveBeenCalled();
  });

  it('TC-5FE-022: logout clears auth state and localStorage', () => {
    useAdminAuthStore.getState().login({ accessToken: 'at', refreshToken: 'rt', storeId: 1, storeName: 'S', adminId: 1, username: 'a' });
    useAdminAuthStore.getState().logout();

    const state = useAdminAuthStore.getState();
    expect(state.isAuthenticated).toBe(false);
    expect(state.accessToken).toBeNull();
    expect(localStorageMock.removeItem).toHaveBeenCalled();
  });

  it('TC-5FE-023: hydrate restores state from localStorage', () => {
    const data = { accessToken: 'at', refreshToken: 'rt', storeId: 1, storeName: 'S', adminId: 1, username: 'a' };
    localStorageMock.setItem('admin-auth', JSON.stringify(data));

    useAdminAuthStore.getState().hydrate();

    const state = useAdminAuthStore.getState();
    expect(state.isAuthenticated).toBe(true);
    expect(state.accessToken).toBe('at');
  });
});
