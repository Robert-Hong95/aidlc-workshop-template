# Test Plan - Unit 4-BE (Order + SSE API)

## Unit Overview
- **Stories**: US-C04, US-C05, US-A02, US-S02
- **Total Test Cases**: 22 (OrderService: 10, SseEmitter: 2, TableService endSession: 2, Controller: 8)

## OrderService Tests (10)

- **TC-ORD-001**: createOrder 성공
  - Given: 유효한 storeId, tableId, 메뉴 목록, 활성 세션 존재
  - When: createOrder 호출
  - Then: OrderResponse 반환, totalAmount 계산 정확
  - Story: US-C04 | Status: ⬜

- **TC-ORD-002**: createOrder 세션 자동 생성
  - Given: 활성 세션 없는 테이블
  - When: createOrder 호출
  - Then: 새 세션 생성 후 주문 저장
  - Story: US-C04 | Status: ⬜

- **TC-ORD-003**: getOrdersByTable 성공
  - Given: 세션에 주문 2개 존재
  - When: getOrdersByTable 호출
  - Then: 2개 주문 반환
  - Story: US-C05 | Status: ⬜

- **TC-ORD-004**: getActiveOrders 성공
  - Given: 매장에 PENDING/PREPARING 주문 존재
  - When: getActiveOrders 호출
  - Then: 활성 주문만 반환
  - Story: US-A02 | Status: ⬜

- **TC-ORD-005**: updateOrderStatus 성공 (PENDING→PREPARING)
  - Given: PENDING 상태 주문
  - When: updateOrderStatus(PREPARING) 호출
  - Then: 상태 변경됨
  - Story: US-A02 | Status: ⬜

- **TC-ORD-006**: updateOrderStatus 실패 - 역방향
  - Given: PREPARING 상태 주문
  - When: updateOrderStatus(PENDING) 호출
  - Then: INVALID_ORDER_STATUS 에러
  - Story: US-A02 | Status: ⬜

- **TC-ORD-007**: deleteOrderByAdmin 성공
  - Given: 존재하는 주문
  - When: deleteOrderByAdmin 호출
  - Then: 주문 삭제
  - Story: US-A02 | Status: ⬜

- **TC-ORD-008**: deleteOrderByCustomer 성공 (PENDING)
  - Given: PENDING 상태 주문
  - When: deleteOrderByCustomer 호출
  - Then: 주문 삭제
  - Story: US-C04 | Status: ⬜

- **TC-ORD-009**: deleteOrderByCustomer 실패 (PREPARING)
  - Given: PREPARING 상태 주문
  - When: deleteOrderByCustomer 호출
  - Then: ACCESS_DENIED 에러
  - Story: US-C04 | Status: ⬜

- **TC-ORD-010**: createOrder 실패 - 테이블 없음
  - Given: 존재하지 않는 tableId
  - When: createOrder 호출
  - Then: TABLE_NOT_FOUND 에러
  - Story: US-C04 | Status: ⬜

## SseEmitterService Tests (2)

- **TC-SSE-001**: subscribe 성공
  - Given: storeId, clientType
  - When: subscribe 호출
  - Then: SseEmitter 반환
  - Story: US-S02 | Status: ⬜

- **TC-SSE-002**: publishToStore 성공
  - Given: 구독된 emitter 존재
  - When: publishToStore 호출
  - Then: 이벤트 전송 (에러 없음)
  - Story: US-S02 | Status: ⬜

## TableService endSession Tests (2)

- **TC-END-001**: endSession 성공 - 주문 이관
  - Given: 완료 주문만 있는 세션
  - When: endSession 호출
  - Then: order_history 저장, orders 삭제, 세션 종료
  - Story: US-A02 | Status: ⬜

- **TC-END-002**: endSession 실패 - 미완료 주문
  - Given: PENDING 주문이 있는 세션
  - When: endSession 호출
  - Then: HAS_INCOMPLETE_ORDERS 에러
  - Story: US-A02 | Status: ⬜

## Controller Tests (8)

- **TC-CTRL-001**: POST /api/customer/.../orders 성공 | US-C04 | ⬜
- **TC-CTRL-002**: GET /api/customer/.../orders 성공 | US-C05 | ⬜
- **TC-CTRL-003**: DELETE /api/customer/orders/{id} 성공 | US-C04 | ⬜
- **TC-CTRL-004**: GET /api/admin/stores/{id}/orders 성공 | US-A02 | ⬜
- **TC-CTRL-005**: PUT /api/admin/orders/{id}/status 성공 | US-A02 | ⬜
- **TC-CTRL-006**: DELETE /api/admin/orders/{id} 성공 | US-A02 | ⬜
- **TC-CTRL-007**: GET /api/admin/stores/{id}/sse 성공 | US-S02 | ⬜
- **TC-CTRL-008**: GET /api/customer/.../sse 성공 | US-S02 | ⬜

## Requirements Coverage
| Story | Test Cases | Status |
|-------|-----------|--------|
| US-C04 | TC-ORD-001,002,008,009,010, TC-CTRL-001,003 | ⬜ |
| US-C05 | TC-ORD-003, TC-CTRL-002 | ⬜ |
| US-A02 | TC-ORD-004~007, TC-END-001~002, TC-CTRL-004~006 | ⬜ |
| US-S02 | TC-SSE-001~002, TC-CTRL-007~008 | ⬜ |
