# Unit 2-BE Store/Table API - Business Logic Model

## 1. 매장+관리자 동시 등록 플로우

```
AdminStoreController.createStore(StoreCreateRequest)
  → StoreService.createStore(request)
    1. StoreRepository.findByStoreCode(storeCode)
       → 존재하면: DUPLICATE_STORE_CODE 예외
    2. Store 생성 및 저장
    3. 비밀번호 정책 검증 (영문+숫자+특수문자, 8자 이상)
    4. Admin 생성 (password bcrypt 해싱) 및 저장
    5. return StoreResponse (매장 정보)
```

## 2. 매장 조회 플로우

```
AdminStoreController.getStore(storeId)
  → StoreService.getStore(storeId)
    1. StoreRepository.findById(storeId)
       → 없으면: STORE_NOT_FOUND 예외
    2. return StoreResponse

AdminStoreController.getStores()
  → StoreService.getStores()
    1. StoreRepository.findAll()
    2. return List<StoreResponse>
```

## 3. 테이블 초기 설정 플로우

```
AdminTableController.setupTable(storeId, TableSetupRequest)
  → TableService.setupTable(storeId, request)
    1. StoreRepository.findById(storeId) → 매장 확인
    2. StoreTableRepository.findByStoreIdAndTableNo(storeId, tableNo)
       → 존재하면: DUPLICATE_TABLE_NO 예외
    3. 비밀번호 bcrypt 해싱
    4. StoreTable 생성 및 저장
    5. return TableResponse
    (세션은 시작하지 않음 — 고객 첫 주문 시 자동 시작)
```

## 4. 테이블 목록 조회 플로우

```
AdminTableController.getTables(storeId)
  → TableService.getTables(storeId)
    1. StoreTableRepository.findAllByStoreId(storeId)
    2. 각 테이블의 활성 세션 조회 (있으면 세션 정보 포함)
    3. return List<TableResponse>
```

## 5. 테이블 세션 시작 플로우

```
TableService.startSession(tableId)
  1. 기존 활성 세션 확인 → 있으면 그대로 반환
  2. TableSession.start(tableId) → 저장
  3. return TableSession
```

## 6. 테이블 이용 완료 (세션 종료) 플로우

```
AdminTableController.endSession(tableId)
  → TableService.endSession(tableId)
    1. TableSessionRepository.findByTableIdAndEndedAtIsNull(tableId)
       → 없으면: SESSION_NOT_FOUND 예외
    2. OrderRepository.findBySessionId(sessionId) → 세션 주문 조회
    3. 각 주문을 JSON 직렬화 → OrderHistory 생성 및 저장
    4. OrderRepository.deleteAll(orders) → 현재 주문 삭제
    5. session.end() → 세션 종료
    6. return void
```

## 7. 과거 주문 내역 조회 플로우

```
AdminTableController.getOrderHistory(tableId, dateFrom, dateTo, lastId, size)
  → TableService.getOrderHistory(tableId, dateFrom, dateTo, lastId, size)
    1. Cursor 기반 페이지네이션:
       - lastId가 null이면 첫 페이지
       - lastId가 있으면 해당 ID 이후부터 조회
    2. OrderHistoryRepository.findByTableIdAndCompletedAtBetween(...)
    3. return OrderHistoryPage (items, hasNext, lastId)
```

---

## 8. DTO 설계

### Request
```java
public record StoreCreateRequest(
    @NotBlank String storeCode,
    @NotBlank String name,
    @NotBlank String adminUsername,
    @NotBlank String adminPassword    // 비밀번호 정책 검증
) {}

public record TableSetupRequest(
    @NotNull Integer tableNo,
    @NotBlank String password
) {}
```

### Response
```java
public record StoreResponse(Long id, String storeCode, String name, LocalDateTime createdAt) {}
public record TableResponse(Long id, int tableNo, Long activeSessionId, LocalDateTime createdAt) {}
public record OrderHistoryResponse(Long id, Long tableId, int totalAmount, Object orderData, LocalDateTime orderedAt, LocalDateTime completedAt) {}
public record OrderHistoryPage(List<OrderHistoryResponse> items, boolean hasNext, Long lastId) {}
```

---

## 9. API 엔드포인트

| Method | Path | Auth | 설명 |
|--------|------|------|------|
| POST | `/api/stores` | 불필요 | 매장+관리자 동시 등록 |
| GET | `/api/admin/stores` | ADMIN | 매장 목록 조회 |
| GET | `/api/admin/stores/{storeId}` | ADMIN | 매장 상세 조회 |
| POST | `/api/admin/stores/{storeId}/tables` | ADMIN | 테이블 초기 설정 |
| GET | `/api/admin/stores/{storeId}/tables` | ADMIN | 테이블 목록 조회 |
| POST | `/api/admin/tables/{tableId}/end-session` | ADMIN | 테이블 이용 완료 |
| GET | `/api/admin/tables/{tableId}/history` | ADMIN | 과거 주문 내역 조회 |
