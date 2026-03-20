# Business Logic Model - Unit 4-BE (Order + SSE API)

## 1. 주문 생성
- 고객이 storeId, tableId, 메뉴 목록(menuId, quantity) 전송
- 활성 세션 없으면 자동 생성 (TableSession)
- Order + OrderItem 저장, totalAmount 계산 (unit_price × quantity 합계)
- SSE: NEW_ORDER 이벤트 → 매장 관리자에게 전송

## 2. 주문 상태 변경
- 상태 전이: PENDING → PREPARING → COMPLETED (3단계, 순방향만)
- 역방향 전이 시 INVALID_ORDER_STATUS 에러
- SSE: ORDER_STATUS_CHANGED → 관리자 + 해당 테이블 고객

## 3. 주문 삭제
- 관리자: 모든 상태에서 삭제 가능
- 고객: PENDING 상태에서만 취소 가능
- SSE: ORDER_DELETED → 관리자 + 해당 테이블 고객

## 4. 주문 조회
- 고객: 현재 세션 주문 목록 (sessionId 기반)
- 관리자: 매장 활성 주문 목록 (PENDING, PREPARING)

## 5. SSE 실시간 통신
- Heartbeat: 15초 간격
- 타임아웃: 10분 (클라이언트 자동 재연결)
- 매장별 관리자 연결 풀, 매장+테이블별 고객 연결 풀

## 6. endSession 주문 이관 (TableService 수정)
- 미완료 주문(PENDING/PREPARING) 있으면 세션 종료 차단
- 완료된 주문 → JSON serialize → order_history 저장
- orders/order_items 삭제
- 세션 종료 (endedAt 설정)
