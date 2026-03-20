# Contract/Interface Definition - Unit 4-BE (Order + SSE API)

## Unit Context
- **Stories**: US-C04, US-C05, US-A02, US-S02
- **Dependencies**: Unit 0 (common), Unit 2 (TableSession, StoreTable), Unit 3 (Menu)
- **Database Entities**: Order, OrderItem (existing schema), OrderHistory (existing)

## Business Logic Layer

### OrderService
- `createOrder(storeId, tableId, request) -> OrderResponse`: 주문 생성 + SSE
  - 세션 없으면 자동 생성
  - Raises: TABLE_NOT_FOUND, MENU_NOT_FOUND
- `getOrdersByTable(storeId, tableId) -> List<OrderResponse>`: 현재 세션 주문 조회
- `getActiveOrders(storeId) -> List<OrderResponse>`: 매장 활성 주문 (PENDING/PREPARING)
- `updateOrderStatus(orderId, status) -> OrderResponse`: 상태 변경 + SSE
  - Raises: ORDER_NOT_FOUND, INVALID_ORDER_STATUS
- `deleteOrderByAdmin(orderId) -> void`: 관리자 삭제 + SSE
  - Raises: ORDER_NOT_FOUND
- `deleteOrderByCustomer(orderId) -> void`: 고객 취소 (PENDING만) + SSE
  - Raises: ORDER_NOT_FOUND, ACCESS_DENIED

### SseEmitterService
- `subscribe(storeId, clientType, clientId) -> SseEmitter`: SSE 연결
- `publishToStore(storeId, event) -> void`: 매장 관리자에게 전송
- `publishToTable(storeId, tableId, event) -> void`: 테이블 고객에게 전송

### TableService (수정)
- `endSession(tableId) -> void`: 미완료 주문 차단 + 완료 주문 이관 + 세션 종료

## API Layer

### CustomerOrderController
- `POST /api/customer/stores/{storeId}/tables/{tableId}/orders` → createOrder
- `GET /api/customer/stores/{storeId}/tables/{tableId}/orders` → getOrdersByTable
- `DELETE /api/customer/orders/{orderId}` → deleteOrderByCustomer

### AdminOrderController
- `GET /api/admin/stores/{storeId}/orders` → getActiveOrders
- `PUT /api/admin/orders/{orderId}/status` → updateOrderStatus
- `DELETE /api/admin/orders/{orderId}` → deleteOrderByAdmin

### SSE Controllers
- `GET /api/customer/stores/{storeId}/tables/{tableId}/sse` → subscribe (CUSTOMER)
- `GET /api/admin/stores/{storeId}/sse` → subscribe (ADMIN)

## Repository Layer

### OrderRepository
- `findBySessionIdOrderByCreatedAtDesc(sessionId) -> List<Order>`
- `findByStoreIdAndStatusIn(storeId, statuses) -> List<Order>`
- `findBySessionId(sessionId) -> List<Order>`

### OrderItemRepository
- `findByOrderId(orderId) -> List<OrderItem>`
