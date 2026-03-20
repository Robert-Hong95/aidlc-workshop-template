# Unit 4-BE Order+SSE API - Business Logic Model

## 1. 주문 생성
```
CustomerOrderController.createOrder(storeId, tableId, OrderCreateRequest)
  → OrderService.createOrder(storeId, tableId, request)
    1. Store 존재 확인
    2. StoreTable 존재 확인
    3. TableService.startSession(tableId) → 세션 자동 시작/기존 반환
    4. 각 orderItem의 메뉴 존재+가격 검증
    5. totalAmount 계산
    6. Order 생성 (status=PENDING)
    7. OrderItem 벌크 저장
    8. SSE: publishToStore(storeId, NEW_ORDER 이벤트)
    9. return OrderResponse
```

## 2. 주문 상태 변경
```
AdminOrderController.updateOrderStatus(orderId, status)
  → OrderService.updateOrderStatus(orderId, status)
    1. Order 존재 확인
    2. 상태 전이 검증: PENDING→CONFIRMED→PREPARING→COMPLETED
    3. status 업데이트
    4. SSE: publishToTable(storeId, tableId, STATUS_CHANGED 이벤트)
    5. return OrderResponse
```

## 3. 주문 삭제
```
// 관리자
AdminOrderController.deleteOrder(orderId)
  → OrderService.deleteOrder(orderId)
    1. Order 존재 확인
    2. 삭제 (CASCADE로 order_items도 삭제)
    3. SSE: publishToTable + publishToStore (ORDER_DELETED 이벤트)

// 고객 (PENDING만)
CustomerOrderController.deleteOrder(orderId)
  → OrderService.deleteOrderByCustomer(orderId, tableId)
    1. Order 존재 + 본인 테이블 확인
    2. status == PENDING 확인 (아니면 예외)
    3. 삭제
    4. SSE: publishToStore (ORDER_DELETED 이벤트)
```

## 4. 주문 조회
```
// 세션별 (고객)
CustomerOrderController.getOrders(sessionId)
  → OrderService.getOrdersBySession(sessionId)

// 매장 활성 주문 (관리자)
AdminOrderController.getActiveOrders(storeId)
  → OrderService.getActiveOrdersByStore(storeId)
```

## 5. SSE 구독
```
CustomerSseController.subscribe(storeId, tableId) → GET /api/customer/sse/subscribe
  → SseEmitterService.subscribe(storeId, "TABLE", tableId)

AdminSseController.subscribe(storeId) → GET /api/admin/sse/subscribe
  → SseEmitterService.subscribe(storeId, "ADMIN", adminId)
```

## 6. API 엔드포인트

| Method | Path | Auth | 설명 |
|--------|------|------|------|
| POST | `/api/customer/stores/{storeId}/tables/{tableId}/orders` | TABLE | 주문 생성 |
| GET | `/api/customer/sessions/{sessionId}/orders` | TABLE | 세션별 주문 조회 |
| DELETE | `/api/customer/orders/{orderId}` | TABLE | 고객 주문 삭제 (PENDING만) |
| GET | `/api/admin/stores/{storeId}/orders` | ADMIN | 매장 활성 주문 |
| PUT | `/api/admin/orders/{orderId}/status` | ADMIN | 주문 상태 변경 |
| DELETE | `/api/admin/orders/{orderId}` | ADMIN | 주문 삭제 |
| GET | `/api/customer/sse/subscribe` | TABLE | 고객 SSE 구독 |
| GET | `/api/admin/sse/subscribe` | ADMIN | 관리자 SSE 구독 |

## 7. DTO
```java
record OrderCreateRequest(List<OrderItemRequest> items) {}
record OrderItemRequest(@NotNull Long menuId, @NotNull @Min(1) Integer quantity) {}
record OrderStatusRequest(@NotBlank String status) {}
record OrderResponse(Long id, Long tableId, int totalAmount, String status, List<OrderItemResponse> items, LocalDateTime createdAt) {}
record OrderItemResponse(Long id, String menuName, int quantity, int unitPrice) {}
```
