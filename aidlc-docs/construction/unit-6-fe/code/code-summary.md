# Code Summary - Unit 6-FE (Store/Table 관리 UI)

## 생성 완료일: 2026-03-20

## 테스트 결과
- **api-client 추가분: 8 tests passed** (stores 3, tables 5)
- **admin 추가분: 17 tests passed** (store 7, table 10)
- **admin 전체: 31 tests ALL PASSED**

## 생성된 파일

### packages/api-client/ (확장)
| 파일 | 설명 |
|------|------|
| src/types/store.ts | Store, CreateStoreRequest, UpdateStoreRequest |
| src/types/table.ts | StoreTable, CreateTableRequest, OrderHistoryItem, OrderHistoryFilter |
| src/stores.ts | getStores, createStore, updateStore |
| src/tables.ts | getTables, createTable, completeSession, deleteOrder, getOrderHistory |
| __tests__/stores.test.ts | TC-6FE-001~003 |
| __tests__/tables.test.ts | TC-6FE-004~008 |

### apps/admin/ (확장)
| 파일 | 설명 |
|------|------|
| components/features/store/StoreForm.tsx | 매장 등록/수정 폼 (react-hook-form + zod) |
| components/features/store/StoreList.tsx | 매장 목록 테이블 |
| components/features/store/StoreSwitcher.tsx | AdminHeader 매장 전환 드롭다운 |
| components/features/table/ViewToggle.tsx | 리스트/카드 뷰 전환 |
| components/features/table/TableSetupForm.tsx | 테이블 추가 (번호 + 4자리 PIN) |
| components/features/table/SessionCompleteDialog.tsx | 이용 완료 확인 (총 금액 요약) |
| components/features/table/DatePresets.tsx | 날짜 프리셋 (오늘/7일/30일) |
| components/features/table/OrderHistoryModal.tsx | 과거 주문 내역 모달 |
| components/features/table/TableListView.tsx | 테이블 리스트 뷰 |
| components/features/table/TableCardView.tsx | 테이블 카드 뷰 |
| hooks/useStores.ts | 매장 CRUD hook (API deferred) |
| hooks/useTables.ts | 테이블 관리 hook (API deferred) |
| hooks/useOrderHistory.ts | 과거 내역 hook (API deferred) |

## Deferred Items
- StoreManagePage, TableManagePage — API 연동 시 완성
- React Query 실제 연동 — Backend 완성 후
