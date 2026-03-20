# Frontend Components - Unit 6-FE (Store/Table 관리 UI)

## 1. 매장 관리 페이지

### StoreManagePage (app/stores/page.tsx)
- 매장 목록 + 등록/수정 폼
- useStores hook으로 데이터 관리

### StoreList
- Props: `stores`, `onEdit`, `onSelect`
- 매장 목록 테이블 (매장명, 코드, 주소, 수정 버튼)

### StoreForm
- Props: `store?` (수정 시), `onSubmit`, `isLoading`, `onCancel`
- react-hook-form + zod 검증
- 필드: 매장명(필수), 주소(선택), 전화번호(선택)

### StoreSwitcher (AdminHeader 내)
- Props: `stores`, `currentStoreId`, `onSwitch`
- 매장명 클릭 → 드롭다운 표시
- 매장 선택 시 storeId 전환

## 2. 테이블 관리 페이지

### TableManagePage (app/tables/page.tsx)
- 테이블 목록 (리스트/카드 토글) + 추가 폼
- useTables hook으로 데이터 관리

### TableListView
- Props: `tables`, `onComplete`, `onDelete`, `onHistory`
- 테이블 리스트 형태 (번호, 세션상태, 총주문액, 액션 버튼)

### TableCardView
- Props: `tables`, `onComplete`, `onDelete`, `onHistory`
- 테이블 카드 그리드 형태

### ViewToggle
- Props: `mode`, `onChange`
- 리스트/카드 뷰 전환 버튼

### TableSetupForm
- Props: `onSubmit`, `isLoading`
- 필드: 테이블번호(숫자), 비밀번호(4자리 PIN, inputMode="numeric")
- zod 검증: tableNo > 0, password /^\d{4}$/

### TableSessionControl
- Props: `table`, `onComplete`
- 세션 활성 시: "이용 완료" 버튼 표시
- 세션 비활성 시: "비활성" 상태 표시

### SessionCompleteDialog
- Props: `isOpen`, `table`, `totalAmount`, `onConfirm`, `onCancel`
- 총 주문 금액 요약 + 확인/취소

### OrderDeleteButton
- Props: `orderId`, `onDelete`
- ConfirmDialog 연동

### OrderHistoryModal
- Props: `isOpen`, `tableId`, `onClose`
- 날짜 필터: DatePresets (오늘/7일/30일) + DateRangePicker
- 주문 목록: 주문번호, 시각, 메뉴, 총금액, 이용완료시각

### DatePresets
- Props: `onSelect`
- 버튼: 오늘, 최근 7일, 최근 30일

## 3. Hooks

### useStores
- `stores`: 매장 목록 (React Query)
- `createStore(data)`: 매장 등록 mutation
- `updateStore(id, data)`: 매장 수정 mutation

### useTables
- `tables`: 테이블 목록 (React Query, storeId 기반)
- `createTable(data)`: 테이블 추가 mutation
- `completeSession(tableId)`: 이용 완료 mutation
- `deleteOrder(orderId)`: 주문 삭제 mutation

### useOrderHistory
- `history`: 과거 주문 내역 (React Query, tableId + 날짜 필터)
- `filter`: 현재 필터 상태
- `setFilter(filter)`: 필터 변경

## 4. API 연동 포인트

| 컴포넌트 | API Endpoint | Method |
|---------|-------------|--------|
| StoreList | /api/admin/stores | GET |
| StoreForm (등록) | /api/admin/stores | POST |
| StoreForm (수정) | /api/admin/stores/{id} | PUT |
| TableManagePage | /api/admin/tables?storeId={} | GET |
| TableSetupForm | /api/admin/tables | POST |
| SessionCompleteDialog | /api/admin/tables/{id}/complete-session | POST |
| OrderDeleteButton | /api/admin/orders/{id} | DELETE |
| OrderHistoryModal | /api/admin/tables/{id}/history | GET |
