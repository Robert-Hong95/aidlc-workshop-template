# Contract/Interface Definition - Unit 6-FE (Store/Table 관리 UI)

## Unit Context
- **Stories**: US-A05 (매장 관리), US-A03 (테이블 관리)
- **작업 디렉토리**: table-order/frontend/

---

## 1. API Client 확장 (packages/api-client)

### types/store.ts
- `Store { id, name, code, address?, phone?, createdAt }`
- `CreateStoreRequest { name, address?, phone? }`
- `UpdateStoreRequest { name?, address?, phone? }`

### types/table.ts
- `StoreTable { id, storeId, tableNo, sessionActive, currentSessionId?, totalOrderAmount, createdAt }`
- `CreateTableRequest { storeId, tableNo, password }`
- `OrderHistoryItem { orderId, orderNo, orderedAt, items[], totalAmount, completedAt? }`
- `OrderHistoryFilter { from, to }`

### stores.ts
- `getStores(client): Promise<Store[]>`
- `createStore(client, data): Promise<Store>`
- `updateStore(client, id, data): Promise<Store>`

### tables.ts
- `getTables(client, storeId): Promise<StoreTable[]>`
- `createTable(client, data): Promise<StoreTable>`
- `completeSession(client, tableId): Promise<void>`
- `deleteOrder(client, orderId): Promise<void>`
- `getOrderHistory(client, tableId, filter): Promise<OrderHistoryItem[]>`

---

## 2. Admin App Hooks

### hooks/useStores.ts
- `useStores()` → `{ stores, isLoading, createStore, updateStore }`

### hooks/useTables.ts
- `useTables(storeId)` → `{ tables, isLoading, createTable, completeSession, deleteOrder }`

### hooks/useOrderHistory.ts
- `useOrderHistory(tableId)` → `{ history, isLoading, filter, setFilter }`

---

## 3. Admin App Components

### StoreSwitcher — AdminHeader 내 매장 전환 드롭다운
### StoreManagePage — 매장 목록 + 등록/수정 폼
### StoreList — 매장 목록 테이블
### StoreForm — 매장 등록/수정 폼 (react-hook-form + zod)
### TableManagePage — 테이블 목록 (리스트/카드 토글) + 추가
### TableListView / TableCardView — 뷰 모드별 테이블 표시
### ViewToggle — 리스트/카드 전환
### TableSetupForm — 테이블 추가 폼 (번호 + 4자리 PIN)
### SessionCompleteDialog — 이용 완료 확인 (총 금액 요약)
### OrderHistoryModal — 과거 주문 내역 (날짜 필터)
### DatePresets — 프리셋 버튼 (오늘/7일/30일)
