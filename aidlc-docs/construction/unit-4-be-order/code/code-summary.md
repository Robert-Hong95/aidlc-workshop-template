# Code Summary - Unit 4-BE (Order + SSE API)

## 생성된 파일

### Domain: Order, OrderItem, OrderStatus enum
### Repository: OrderRepository, OrderItemRepository
### DTO: OrderCreateRequest, OrderItemRequest, OrderResponse, OrderItemResponse, OrderStatusRequest, SseEvent
### Service: OrderService (6 methods), SseEmitterService (3 methods + heartbeat)
### Controller: CustomerOrderController (3), AdminOrderController (3), SseController (2)
### 수정: TableService.endSession() - 주문 이관 로직 완성, ErrorCode에 HAS_INCOMPLETE_ORDERS 추가

## Tests: 22개 전체 통과
- OrderServiceTest: 10 tests
- SseEmitterServiceTest: 2 tests
- TableServiceTest: +2 tests (TC-END-001, TC-END-002)
- OrderControllerTest: 8 tests
