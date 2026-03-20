# TDD Code Generation Plan - Unit 2-BE (Store/Table API)

## Unit Context
- **Backend Root**: table-order/backend/
- **Source**: src/main/java/com/tableorder/
- **Test**: src/test/java/com/tableorder/
- **Stories**: US-A05 (매장 관리), US-A03 (테이블 관리)

---

## Plan Step 0: Skeleton + DTO
- [ ] 0.1 Entity: TableSession (신규), Store에 updateName 추가
- [ ] 0.2 Repository: TableSessionRepository (신규), StoreTableRepository에 findAllByStoreId 추가
- [ ] 0.3 DTO: StoreCreateRequest, StoreUpdateRequest, StoreResponse, TableSetupRequest, TableResponse, OrderHistoryResponse
- [ ] 0.4 Service: StoreService, TableService (stub)
- [ ] 0.5 Controller: AdminStoreController, AdminTableController (stub)
- [ ] 0.6 Verify: 컴파일 확인

## Plan Step 1: StoreService (TDD)
- [ ] 1.1 RED: TC-STORE-001 (등록 성공) → 실패 확인
- [ ] 1.2 GREEN: createStore() 구현
- [ ] 1.3 RED: TC-STORE-002 (중복 코드) → 실패 확인
- [ ] 1.4 GREEN: 중복 검증
- [ ] 1.5 RED: TC-STORE-003~007 (목록/조회/수정/삭제) → 실패 확인
- [ ] 1.6 GREEN: getStores, getStore, updateStore, deleteStore 구현
- [ ] 1.7 REFACTOR + VERIFY

## Plan Step 2: TableService (TDD)
- [ ] 2.1 RED: TC-TABLE-001 (설정 성공) → 실패 확인
- [ ] 2.2 GREEN: setupTable() 구현
- [ ] 2.3 RED: TC-TABLE-002 (중복 번호) → 실패 확인
- [ ] 2.4 GREEN: 중복 검증
- [ ] 2.5 RED: TC-TABLE-003 (목록 조회) → 실패 확인
- [ ] 2.6 GREEN: getTables() 구현
- [ ] 2.7 RED: TC-TABLE-004~005 (세션 종료 성공/실패) → 실패 확인
- [ ] 2.8 GREEN: endSession() 구현
- [ ] 2.9 RED: TC-TABLE-006 (과거 내역) → 실패 확인
- [ ] 2.10 GREEN: getOrderHistory() 구현
- [ ] 2.11 REFACTOR + VERIFY

## Plan Step 3: Controller Layer (TDD)
- [ ] 3.1 RED: TC-STORE-008~010 (Store controller) → 실패 확인
- [ ] 3.2 GREEN: AdminStoreController 구현
- [ ] 3.3 RED: TC-TABLE-007~009 (Table controller) → 실패 확인
- [ ] 3.4 GREEN: AdminTableController 구현
- [ ] 3.5 REFACTOR + VERIFY

## Plan Step 4: Documentation
- [ ] 4.1 Code summary 생성
