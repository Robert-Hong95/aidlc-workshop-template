# Test Plan for Unit 2-BE Store/Table API

## Unit Overview
- **Unit**: Unit 2-BE (Store/Table API)
- **Stories**: US-A05 (매장 관리), US-A03 (테이블 관리)

## Domain Layer Tests

### TableSession
- **TC-ST-001**: 새 세션 생성 시 isActive() == true
  - Given: tableId
  - When: TableSession.start(tableId)
  - Then: isActive() true, endedAt null
  - Status: ⬜ Not Started
- **TC-ST-002**: 세션 종료 시 isActive() == false
  - Given: 활성 세션
  - When: session.end()
  - Then: isActive() false, endedAt != null
  - Status: ⬜ Not Started

## Business Logic Layer Tests

### StoreService.createStore()
- **TC-ST-003**: 정상 매장+관리자 생성
  - Given: 유효한 storeCode, name, adminUsername, adminPassword
  - When: createStore(request)
  - Then: Store 저장, Admin 저장 (비밀번호 해싱), StoreResponse 반환
  - Story: US-A05
  - Status: ⬜ Not Started
- **TC-ST-004**: 중복 storeCode → DUPLICATE_STORE_CODE
  - Given: 이미 존재하는 storeCode
  - When: createStore(request)
  - Then: BusinessException(DUPLICATE_STORE_CODE)
  - Story: US-A05
  - Status: ⬜ Not Started
- **TC-ST-005**: 비밀번호 정책 위반 → INVALID_INPUT
  - Given: 정책 미달 비밀번호 (예: "1234")
  - When: createStore(request)
  - Then: BusinessException(INVALID_INPUT)
  - Story: US-A05
  - Status: ⬜ Not Started

### StoreService.getStore()
- **TC-ST-006**: 정상 매장 조회
  - Given: 존재하는 storeId
  - When: getStore(storeId)
  - Then: StoreResponse 반환
  - Status: ⬜ Not Started
- **TC-ST-007**: 존재하지 않는 매장 → STORE_NOT_FOUND
  - Given: 존재하지 않는 storeId
  - When: getStore(storeId)
  - Then: BusinessException(STORE_NOT_FOUND)
  - Status: ⬜ Not Started

### StoreService.getStores()
- **TC-ST-008**: 매장 목록 조회
  - Given: 매장 2개 존재
  - When: getStores()
  - Then: 2개 StoreResponse 반환
  - Status: ⬜ Not Started

### TableService.setupTable()
- **TC-ST-009**: 정상 테이블 설정
  - Given: 존재하는 storeId, 유효한 tableNo, password
  - When: setupTable(storeId, request)
  - Then: StoreTable 저장 (비밀번호 해싱), TableResponse 반환
  - Story: US-A03
  - Status: ⬜ Not Started
- **TC-ST-010**: 중복 테이블 번호 → DUPLICATE_TABLE_NO
  - Given: 이미 존재하는 storeId+tableNo
  - When: setupTable(storeId, request)
  - Then: BusinessException(DUPLICATE_TABLE_NO)
  - Story: US-A03
  - Status: ⬜ Not Started
- **TC-ST-011**: 존재하지 않는 매장 → STORE_NOT_FOUND
  - Given: 존재하지 않는 storeId
  - When: setupTable(storeId, request)
  - Then: BusinessException(STORE_NOT_FOUND)
  - Story: US-A03
  - Status: ⬜ Not Started

### TableService.getTables()
- **TC-ST-012**: 테이블 목록 + 활성 세션 조회
  - Given: 매장에 테이블 2개, 1개에 활성 세션
  - When: getTables(storeId)
  - Then: 2개 TableResponse, 1개에 activeSessionId 포함
  - Story: US-A03
  - Status: ⬜ Not Started

### TableService.startSession()
- **TC-ST-013**: 새 세션 시작
  - Given: 활성 세션 없는 테이블
  - When: startSession(tableId)
  - Then: 새 TableSession 생성 및 반환
  - Status: ⬜ Not Started
- **TC-ST-014**: 이미 활성 세션 있으면 기존 반환
  - Given: 활성 세션 있는 테이블
  - When: startSession(tableId)
  - Then: 기존 세션 반환
  - Status: ⬜ Not Started

### TableService.endSession()
- **TC-ST-015**: 정상 세션 종료 (주문 이력 이동)
  - Given: 활성 세션 + 주문 2건
  - When: endSession(tableId)
  - Then: OrderHistory 2건 저장, orders 삭제, session.end()
  - Story: US-A03
  - Status: ⬜ Not Started
- **TC-ST-016**: 활성 세션 없음 → SESSION_NOT_FOUND
  - Given: 활성 세션 없는 테이블
  - When: endSession(tableId)
  - Then: BusinessException(SESSION_NOT_FOUND)
  - Status: ⬜ Not Started
- **TC-ST-017**: 주문 없는 세션 종료
  - Given: 활성 세션 + 주문 0건
  - When: endSession(tableId)
  - Then: session.end() (이력 이동 없이 종료)
  - Status: ⬜ Not Started

### TableService.getOrderHistory()
- **TC-ST-018**: Cursor 기반 첫 페이지 조회
  - Given: OrderHistory 3건, size=2
  - When: getOrderHistory(tableId, null, null, null, 2)
  - Then: 2건 반환, hasNext=true, lastId 설정
  - Status: ⬜ Not Started
- **TC-ST-019**: Cursor 기반 다음 페이지 조회
  - Given: OrderHistory 3건, lastId 지정
  - When: getOrderHistory(tableId, null, null, lastId, 2)
  - Then: 1건 반환, hasNext=false
  - Status: ⬜ Not Started

## Infrastructure Layer Tests

### RateLimitInterceptor
- **TC-ST-020**: 제한 내 요청 허용
  - Given: 동일 IP에서 9회 요청
  - When: 10번째 요청
  - Then: 허용 (true 반환)
  - Status: ⬜ Not Started
- **TC-ST-021**: 제한 초과 시 429 응답
  - Given: 동일 IP에서 10회 요청 완료
  - When: 11번째 요청
  - Then: 429 응답, false 반환
  - Status: ⬜ Not Started

## Requirements Coverage
| Requirement | Test Cases | Status |
|------------|------------|--------|
| BR-ST-01 매장 등록 | TC-ST-003, 004, 005 | ⬜ |
| BR-ST-02 매장 조회 | TC-ST-006, 007, 008 | ⬜ |
| BR-ST-03 테이블 설정 | TC-ST-009, 010, 011 | ⬜ |
| BR-ST-04 세션 관리 | TC-ST-013, 014 | ⬜ |
| BR-ST-05 이용 완료 | TC-ST-015, 016, 017 | ⬜ |
| BR-ST-06 주문 내역 | TC-ST-018, 019 | ⬜ |
| NFR-ST-01 Rate Limit | TC-ST-020, 021 | ⬜ |
