# Contract/Interface Definition for Unit 2-BE Store/Table API

## Unit Context
- **Stories**: US-A05 (매장 관리), US-A03 (테이블 관리)
- **Dependencies**: Unit 1-BE (Auth — Store, StoreTable, AdminRepository 참조)
- **Database Entities**: Store (확장), StoreTable (확장), TableSession (신규), OrderHistory (신규)

## Business Logic Layer

### StoreService
- `createStore(StoreCreateRequest) -> StoreResponse`: 매장+관리자 동시 생성
  - Args: storeCode, name, adminUsername, adminPassword
  - Returns: StoreResponse (id, storeCode, name, createdAt)
  - Raises: DUPLICATE_STORE_CODE, INVALID_INPUT (비밀번호 정책 위반)
- `getStore(Long storeId) -> StoreResponse`: 매장 상세 조회
  - Returns: StoreResponse
  - Raises: STORE_NOT_FOUND
- `getStores() -> List<StoreResponse>`: 매장 목록 조회
  - Returns: List<StoreResponse>

### TableService
- `setupTable(Long storeId, TableSetupRequest) -> TableResponse`: 테이블 초기 설정
  - Args: storeId, tableNo, password
  - Returns: TableResponse (id, tableNo, activeSessionId, createdAt)
  - Raises: STORE_NOT_FOUND, DUPLICATE_TABLE_NO
- `getTables(Long storeId) -> List<TableResponse>`: 테이블 목록 (활성 세션 포함)
  - Returns: List<TableResponse>
- `startSession(Long tableId) -> TableSession`: 세션 시작 (이미 활성이면 반환)
  - Returns: TableSession
  - Raises: TABLE_NOT_FOUND
- `endSession(Long tableId) -> void`: 세션 종료 (주문 이력 이동)
  - Raises: SESSION_NOT_FOUND
- `getOrderHistory(Long tableId, LocalDateTime dateFrom, LocalDateTime dateTo, Long lastId, int size) -> OrderHistoryPage`: 과거 주문 내역
  - Returns: OrderHistoryPage (items, hasNext, lastId)

## API Layer

### AdminStoreController
- `POST /api/stores` — createStore (인증 불필요)
- `GET /api/admin/stores` — getStores (ADMIN)
- `GET /api/admin/stores/{storeId}` — getStore (ADMIN)

### AdminTableController
- `POST /api/admin/stores/{storeId}/tables` — setupTable (ADMIN)
- `GET /api/admin/stores/{storeId}/tables` — getTables (ADMIN)
- `POST /api/admin/tables/{tableId}/end-session` — endSession (ADMIN)
- `GET /api/admin/tables/{tableId}/history` — getOrderHistory (ADMIN)

## Repository Layer

### StoreRepository (확장)
- `existsByStoreCode(String storeCode) -> boolean`

### StoreTableRepository (확장)
- `findAllByStoreId(Long storeId) -> List<StoreTable>`

### TableSessionRepository (신규)
- `findByTableIdAndEndedAtIsNull(Long tableId) -> Optional<TableSession>`

### OrderHistoryRepository (신규)
- `findByTableIdAndCriteria(Long tableId, LocalDateTime dateFrom, LocalDateTime dateTo, Long lastId, int size) -> List<OrderHistory>`

## Infrastructure Layer

### RateLimitInterceptor (신규)
- `preHandle(request, response, handler) -> boolean`: IP 기반 분당 10회 제한
