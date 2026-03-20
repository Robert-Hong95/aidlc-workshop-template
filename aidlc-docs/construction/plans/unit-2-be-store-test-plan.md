# Test Plan - Unit 2-BE (Store/Table API)

## Unit Overview
- **Unit**: Unit 2-BE Store/Table API
- **Stories**: US-A05 (매장 관리), US-A03 (테이블 관리)

---

## Service Layer Tests

### StoreService

- **TC-STORE-001**: 매장 등록 성공
  - Given: "NEWSTORE" 코드가 존재하지 않음
  - When: createStore("NEWSTORE", "새매장")
  - Then: StoreResponse 반환 (id, storeCode, name)
  - Story: US-A05 / Status: ⬜

- **TC-STORE-002**: 중복 매장 코드 등록 실패
  - Given: "STORE01" 코드가 이미 존재
  - When: createStore("STORE01", "매장")
  - Then: DUPLICATE_STORE_CODE 예외
  - Story: US-A05 / Status: ⬜

- **TC-STORE-003**: 매장 목록 조회
  - Given: 매장 2개 존재
  - When: getStores()
  - Then: 2개 StoreResponse 리스트 반환
  - Story: US-A05 / Status: ⬜

- **TC-STORE-004**: 매장 단건 조회 성공
  - Given: 매장 존재
  - When: getStore(1L)
  - Then: StoreResponse 반환
  - Story: US-A05 / Status: ⬜

- **TC-STORE-005**: 매장 조회 실패 - 없는 매장
  - Given: 매장 ID 999 없음
  - When: getStore(999L)
  - Then: STORE_NOT_FOUND 예외
  - Story: US-A05 / Status: ⬜

- **TC-STORE-006**: 매장 수정 성공
  - Given: 매장 존재
  - When: updateStore(1L, "수정매장")
  - Then: 수정된 StoreResponse 반환
  - Story: US-A05 / Status: ⬜

- **TC-STORE-007**: 매장 삭제 성공
  - Given: 매장 존재
  - When: deleteStore(1L)
  - Then: 정상 삭제
  - Story: US-A05 / Status: ⬜

### TableService

- **TC-TABLE-001**: 테이블 설정 성공
  - Given: 매장 존재, 테이블 번호 3 없음
  - When: setupTable(1L, tableNo=3, password="test1234")
  - Then: TableResponse 반환, 비밀번호 bcrypt 해싱 저장
  - Story: US-A03 / Status: ⬜

- **TC-TABLE-002**: 중복 테이블 번호 설정 실패
  - Given: 매장에 테이블 1 이미 존재
  - When: setupTable(1L, tableNo=1, password="test1234")
  - Then: DUPLICATE_TABLE_NO 예외
  - Story: US-A03 / Status: ⬜

- **TC-TABLE-003**: 테이블 목록 조회
  - Given: 매장에 테이블 2개 존재
  - When: getTables(1L)
  - Then: 2개 TableResponse 리스트 반환
  - Story: US-A03 / Status: ⬜

- **TC-TABLE-004**: 세션 종료 성공
  - Given: 테이블에 활성 세션 존재
  - When: endSession(1L)
  - Then: 세션 endedAt 설정, 주문→이력 이동
  - Story: US-A03 / Status: ⬜

- **TC-TABLE-005**: 세션 종료 실패 - 활성 세션 없음
  - Given: 테이블에 활성 세션 없음
  - When: endSession(1L)
  - Then: SESSION_NOT_FOUND 예외
  - Story: US-A03 / Status: ⬜

- **TC-TABLE-006**: 과거 주문 내역 조회
  - Given: 테이블에 과거 이력 존재
  - When: getOrderHistory(1L, dateFrom, dateTo)
  - Then: OrderHistoryResponse 리스트 반환
  - Story: US-A03 / Status: ⬜

---

## Controller Layer Tests

- **TC-STORE-008**: POST /api/admin/stores 성공 (200)
  - Story: US-A05 / Status: ⬜
- **TC-STORE-009**: POST /api/admin/stores 실패 - 중복 (409)
  - Story: US-A05 / Status: ⬜
- **TC-STORE-010**: GET /api/admin/stores 성공 (200)
  - Story: US-A05 / Status: ⬜
- **TC-TABLE-007**: POST /api/admin/stores/{storeId}/tables 성공 (200)
  - Story: US-A03 / Status: ⬜
- **TC-TABLE-008**: POST /api/admin/tables/{tableId}/end-session 성공 (200)
  - Story: US-A03 / Status: ⬜
- **TC-TABLE-009**: GET /api/admin/tables/{tableId}/order-history 성공 (200)
  - Story: US-A03 / Status: ⬜

---

## Requirements Coverage

| Story | Test Cases | Status |
|-------|-----------|--------|
| US-A05 (매장 관리) | TC-STORE-001~010 | ⬜ Pending |
| US-A03 (테이블 관리) | TC-TABLE-001~009 | ⬜ Pending |
