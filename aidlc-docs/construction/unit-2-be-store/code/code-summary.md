# Code Summary - Unit 2-BE (Store/Table API)

## 생성된 파일

### Domain Layer
- `store/domain/TableSession.java` - 테이블 세션 엔티티 (신규)
- `store/domain/Store.java` - updateName() 메서드 추가 (수정)
- `order/domain/OrderHistory.java` - 주문 이력 엔티티 (신규, endSession 참조용)

### Repository Layer
- `store/repository/TableSessionRepository.java` - findByTableIdAndEndedAtIsNull (신규)
- `store/repository/StoreTableRepository.java` - findAllByStoreId 추가 (수정)
- `order/repository/OrderHistoryRepository.java` - 과거 내역 조회 (신규)

### DTO Layer
- `store/dto/StoreCreateRequest.java`, `StoreUpdateRequest.java`, `StoreResponse.java`
- `store/dto/TableSetupRequest.java`, `TableResponse.java`, `OrderHistoryResponse.java`

### Service Layer
- `store/service/StoreService.java` - 매장 CRUD (5 methods)
- `store/service/TableService.java` - 테이블 설정, 목록, 세션 종료, 과거 내역 (4 methods)

### Controller Layer
- `store/controller/AdminStoreController.java` - 7 endpoints
- `store/controller/AdminTableController.java` - 2 endpoints

### Tests
- `store/service/StoreServiceTest.java` - 7 tests
- `store/service/TableServiceTest.java` - 6 tests
- `store/controller/StoreTableControllerTest.java` - 6 tests

## 테스트 결과: 19개 전체 통과
