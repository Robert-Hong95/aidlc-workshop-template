# Domain Entities - Unit 6-FE (Store/Table 관리 UI)

## API 타입 정의 (packages/api-client/src/types/)

### store.ts
```typescript
export interface Store {
  id: number;
  name: string;
  code: string;
  address?: string;
  phone?: string;
  createdAt: string;
}

export interface CreateStoreRequest {
  name: string;
  address?: string;
  phone?: string;
}

export interface UpdateStoreRequest {
  name?: string;
  address?: string;
  phone?: string;
}
```

### table.ts
```typescript
export interface StoreTable {
  id: number;
  storeId: number;
  tableNo: number;
  sessionActive: boolean;
  currentSessionId?: number;
  totalOrderAmount: number;
  createdAt: string;
}

export interface TableSession {
  id: number;
  tableId: number;
  startedAt: string;
  endedAt?: string;
  totalAmount: number;
}

export interface CreateTableRequest {
  storeId: number;
  tableNo: number;
  password: string; // 4자리 숫자 PIN
}

export interface OrderHistoryItem {
  orderId: number;
  orderNo: string;
  orderedAt: string;
  items: { menuName: string; quantity: number; price: number }[];
  totalAmount: number;
  completedAt?: string;
}

export interface OrderHistoryFilter {
  from: string; // YYYY-MM-DD
  to: string;   // YYYY-MM-DD
}
```
