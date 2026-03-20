# TDD Code Generation Plan for Unit 2-BE Store/Table API

## Unit Context
- **Workspace Root**: /Users/wm-it-22-00429/Wemade/workshop/aidlc-workshop-template
- **Project Type**: Brownfield (Unit 1-BE 코드 존재)
- **Code Base**: table-order/backend/src/main/java/com/tableorder/
- **Test Base**: table-order/backend/src/test/java/com/tableorder/
- **Stories**: US-A05 (매장 관리), US-A03 (테이블 관리)

---

### Plan Step 0: Contract Skeleton + DB Schema ✅
- [x] 0.1~0.16: 모든 스켈레톤 생성 완료

### Plan Step 1: Domain Layer (TDD) ✅
- [x] 1.1: TableSession — RED-GREEN-REFACTOR (TC-ST-001, TC-ST-002)

### Plan Step 2: Business Logic Layer (TDD) ✅
- [x] 2.1: StoreService.createStore() — RED-GREEN-REFACTOR (TC-ST-003, 004, 005)
- [x] 2.2: StoreService.getStore() — RED-GREEN-REFACTOR (TC-ST-006, 007)
- [x] 2.3: StoreService.getStores() — RED-GREEN-REFACTOR (TC-ST-008)
- [x] 2.4: TableService.setupTable() — RED-GREEN-REFACTOR (TC-ST-009, 010, 011)
- [x] 2.5: TableService.getTables() — RED-GREEN-REFACTOR (TC-ST-012)
- [x] 2.6: TableService.startSession() — RED-GREEN-REFACTOR (TC-ST-013, 014)
- [x] 2.7: TableService.endSession() — RED-GREEN-REFACTOR (TC-ST-015, 016, 017)
- [x] 2.8: TableService.getOrderHistory() — RED-GREEN-REFACTOR (TC-ST-018, 019)

### Plan Step 3: Infrastructure Layer (TDD) ✅
- [x] 3.1: RateLimitInterceptor — RED-GREEN-REFACTOR (TC-ST-020, 021)

### Plan Step 4: API Layer + Integration ✅
- [x] 4.1: AdminStoreController 구현
- [x] 4.2: AdminTableController 구현
- [x] 4.3: WebMvcConfigurer에 RateLimitInterceptor 등록
- [x] 4.4: code-generation-summary.md 생성
