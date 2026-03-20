# Unit 2-BE Store/Table API - Code Generation Summary

## TDD 실행 결과
- **총 테스트**: 21개 (Domain 2 + StoreService 6 + TableService 11 + RateLimitInterceptor 2)
- **통과**: 21개
- **실패**: 0개

## 생성/수정 파일

### 신규 생성 (17개)
- `table/domain/TableSession.java` — 세션 엔티티
- `table/domain/OrderHistory.java` — 주문 이력 엔티티
- `table/repository/TableSessionRepository.java`
- `table/repository/OrderHistoryRepository.java`
- `table/service/TableService.java` — 5개 메서드
- `table/controller/AdminTableController.java` — 4개 엔드포인트
- `table/dto/TableSetupRequest.java`
- `table/dto/TableResponse.java`
- `table/dto/OrderHistoryResponse.java`
- `table/dto/OrderHistoryPage.java`
- `store/service/StoreService.java` — 3개 메서드
- `store/controller/AdminStoreController.java` — 3개 엔드포인트
- `store/dto/StoreCreateRequest.java`
- `store/dto/StoreResponse.java`
- `order/domain/Order.java` — 참조용 최소 엔티티
- `order/domain/OrderItem.java` — 참조용 최소 엔티티
- `order/repository/OrderRepository.java`
- `order/repository/OrderItemRepository.java`
- `common/config/RateLimitInterceptor.java`
- `common/config/WebConfig.java`

### 수정 (5개)
- `store/domain/Store.java` — createdAt, updatedAt 추가
- `store/domain/StoreTable.java` — createdAt 추가
- `store/repository/StoreRepository.java` — existsByStoreCode 추가
- `store/repository/StoreTableRepository.java` — findAllByStoreId 추가
- `common/config/SecurityConfig.java` — POST /api/stores permitAll
- `common/exception/ErrorCode.java` — RATE_LIMIT_EXCEEDED 추가
- `auth/domain/Admin.java` — @UniqueConstraint columnNames 수정

### 테스트 (3개)
- `table/domain/TableSessionTest.java` — 2 tests
- `store/service/StoreServiceTest.java` — 6 tests
- `table/service/TableServiceTest.java` — 11 tests
- `common/config/RateLimitInterceptorTest.java` — 2 tests

### 버그 수정
- `auth/service/AuthServiceTest.java` — setUp()에서 entity id reflection 설정 (Map.of NPE 수정)
- `@UniqueConstraint(columns=...)` → `@UniqueConstraint(columnNames=...)` 수정 (Admin, StoreTable)
