# Unit 4-BE Order+SSE API - Code Generation Summary

## TDD 실행 결과
- **총 테스트**: 17개 (Order domain 4 + OrderService 10 + SseEmitterService 3)
- **통과**: 17개
- **실패**: 0개

## 생성 파일

### 수정 (3개 — Unit 2 참조용에서 확장)
- `order/domain/Order.java` — 생성자, 상태 전이 로직, 상수 추가
- `order/domain/OrderItem.java` — 생성자 추가
- `order/repository/OrderRepository.java` — findAllByStoreIdAndStatusNot 추가

### 신규 (10개)
- `order/dto/` — 6개 DTO
- `order/service/OrderService.java` — 6개 메서드
- `order/service/SseEmitterService.java` — subscribe, publishToStore, publishToTable
- `order/controller/CustomerOrderController.java` — 4개 엔드포인트
- `order/controller/AdminOrderController.java` — 4개 엔드포인트

### 수정 (1개)
- `common/config/SecurityConfig.java` — SSE 엔드포인트 permitAll

### 테스트 (3개)
- `order/domain/OrderTest.java` — 4 tests
- `order/service/OrderServiceTest.java` — 10 tests
- `order/service/SseEmitterServiceTest.java` — 3 tests
