# TDD Code Generation Plan - Unit 6-FE (Store/Table 관리 UI)

## Plan Step 0: API Client 타입 + 함수 확장
- [x] 0.1: types/store.ts, types/table.ts 생성
- [x] 0.2: stores.ts, tables.ts 스켈레톤 생성
- [x] 0.3: index.ts export 업데이트

## Plan Step 1: API Client TDD (stores.ts, tables.ts)
- [x] 1.1: stores.ts RED-GREEN-REFACTOR (TC-6FE-001~003)
- [x] 1.2: tables.ts RED-GREEN-REFACTOR (TC-6FE-004~008)

## Plan Step 2: Admin Components — Store (TDD)
- [x] 2.1: StoreForm RED-GREEN-REFACTOR (TC-6FE-009~011)
- [x] 2.2: StoreList RED-GREEN-REFACTOR (TC-6FE-012~013)
- [x] 2.3: StoreSwitcher RED-GREEN-REFACTOR (TC-6FE-014~015)
- [x] 2.4: StoreManagePage — deferred (API 연동 시)

## Plan Step 3: Admin Components — Table (TDD)
- [x] 3.1: ViewToggle RED-GREEN-REFACTOR (TC-6FE-019)
- [x] 3.2: TableSetupForm RED-GREEN-REFACTOR (TC-6FE-016~018)
- [x] 3.3: SessionCompleteDialog RED-GREEN-REFACTOR (TC-6FE-020~021)
- [x] 3.4: DatePresets RED-GREEN-REFACTOR (TC-6FE-024~025)
- [x] 3.5: OrderHistoryModal RED-GREEN-REFACTOR (TC-6FE-022~023)
- [x] 3.6: TableListView + TableCardView
- [x] 3.7: TableManagePage — deferred (API 연동 시)

## Plan Step 4: Hooks (TDD)
- [x] 4.1: useStores hook
- [x] 4.2: useTables hook
- [x] 4.3: useOrderHistory hook

## Plan Step 5: Documentation
- [x] 5.1: Code summary 생성
- [x] 5.2: Plan 체크박스 최종 업데이트
