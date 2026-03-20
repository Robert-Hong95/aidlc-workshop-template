import { describe, it, expect, beforeEach, vi } from 'vitest';
import { useTableAuthStore } from '../../stores/auth-store';

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
  useTableAuthStore.setState({
    accessToken: null, refreshToken: null, storeId: null, storeName: null,
    storeCode: null, tableId: null, tableNo: null, isAuthenticated: false,
  });
});

describe('useTableAuthStore', () => {
  it('TC-5FE-024: login sets table auth state and persists', () => {
    const response = { accessToken: 'at', refreshToken: 'rt', storeId: 1, storeName: 'Test', tableId: 5, tableNo: 3 };
    useTableAuthStore.getState().login(response, 'S001');

    const state = useTableAuthStore.getState();
    expect(state.isAuthenticated).toBe(true);
    expect(state.tableNo).toBe(3);
    expect(state.storeCode).toBe('S001');
    expect(localStorageMock.setItem).toHaveBeenCalled();
  });

  it('TC-5FE-025: logout clears table auth state', () => {
    useTableAuthStore.getState().login({ accessToken: 'at', refreshToken: 'rt', storeId: 1, storeName: 'S', tableId: 1, tableNo: 1 }, 'S001');
    useTableAuthStore.getState().logout();

    const state = useTableAuthStore.getState();
    expect(state.isAuthenticated).toBe(false);
    expect(state.accessToken).toBeNull();
    expect(localStorageMock.removeItem).toHaveBeenCalled();
  });

  it('TC-5FE-026: hydrate restores table auth from localStorage', () => {
    const data = { accessToken: 'at', refreshToken: 'rt', storeId: 1, storeName: 'S', storeCode: 'S001', tableId: 1, tableNo: 1 };
    localStorageMock.setItem('table-auth', JSON.stringify(data));

    useTableAuthStore.getState().hydrate();

    const state = useTableAuthStore.getState();
    expect(state.isAuthenticated).toBe(true);
    expect(state.storeCode).toBe('S001');
  });
});
