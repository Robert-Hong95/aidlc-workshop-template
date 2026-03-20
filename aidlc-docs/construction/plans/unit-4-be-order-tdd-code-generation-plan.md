# TDD Code Generation Plan - Unit 4-BE (Order + SSE API)

## Unit Context
- **Workspace Root**: /Users/wd-il000534/IdeaProjects/aidlc-workshop-template
- **Backend Root**: table-order/backend/
- **Stories**: US-C04, US-C05, US-A02, US-S02

## Plan Step 0: Skeleton Generation
- [ ] Order entity, OrderItem entity, OrderStatus enum
- [ ] OrderRepository, OrderItemRepository
- [ ] DTOs: OrderCreateRequest, OrderItemRequest, OrderResponse, OrderItemResponse, OrderStatusRequest, SseEvent
- [ ] OrderService stub
- [ ] SseEmitterService stub
- [ ] CustomerOrderController, AdminOrderController, CustomerSseController, AdminSseController stubs
- [ ] ErrorCode 추가: HAS_INCOMPLETE_ORDERS
- [ ] Verify compilation

## Plan Step 1: OrderService TDD (TC-ORD-001~010)
- [ ] RED/GREEN: TC-ORD-001 createOrder 성공
- [ ] RED/GREEN: TC-ORD-002 createOrder 세션 자동 생성
- [ ] RED/GREEN: TC-ORD-003 getOrdersByTable
- [ ] RED/GREEN: TC-ORD-004 getActiveOrders
- [ ] RED/GREEN: TC-ORD-005 updateOrderStatus 성공
- [ ] RED/GREEN: TC-ORD-006 updateOrderStatus 역방향 실패
- [ ] RED/GREEN: TC-ORD-007 deleteOrderByAdmin
- [ ] RED/GREEN: TC-ORD-008 deleteOrderByCustomer 성공
- [ ] RED/GREEN: TC-ORD-009 deleteOrderByCustomer 실패
- [ ] RED/GREEN: TC-ORD-010 createOrder 테이블 없음
- [ ] VERIFY: 10 tests pass

## Plan Step 2: SseEmitterService TDD (TC-SSE-001~002)
- [ ] RED/GREEN: TC-SSE-001 subscribe
- [ ] RED/GREEN: TC-SSE-002 publishToStore
- [ ] VERIFY: 2 tests pass

## Plan Step 3: TableService endSession 수정 TDD (TC-END-001~002)
- [ ] Update TableServiceTest with TC-END-001, TC-END-002
- [ ] RED/GREEN: TC-END-001 endSession 주문 이관
- [ ] RED/GREEN: TC-END-002 endSession 미완료 차단
- [ ] Modify TableService.endSession() with order migration logic
- [ ] VERIFY: all TableService tests pass

## Plan Step 4: Controller Layer TDD (TC-CTRL-001~008)
- [ ] Write all controller tests
- [ ] Implement CustomerOrderController, AdminOrderController
- [ ] Implement CustomerSseController, AdminSseController
- [ ] VERIFY: 8 tests pass

## Plan Step 5: Documentation
- [ ] Create code-summary.md
- [ ] Update test-plan.md statuses
- [ ] Update aidlc-state.md
- [ ] Update audit.md
