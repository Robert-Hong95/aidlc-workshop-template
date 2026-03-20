import { createApiClient } from '@table-order/api-client';
import { useAdminAuthStore } from '../stores/auth-store';

const API_BASE = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

export const apiClient = createApiClient({
  baseUrl: API_BASE,
  getToken: () => useAdminAuthStore.getState().token,
  onUnauthorized: () => useAdminAuthStore.getState().logout(),
  onRefreshToken: async () => null,
});

export { API_BASE };
