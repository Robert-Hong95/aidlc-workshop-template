# Business Rules - Unit 4-BE (Order + SSE API)

## 주문 상태 전이
- PENDING → PREPARING → COMPLETED (순방향만)
- 역방향/건너뛰기 → INVALID_ORDER_STATUS

## 주문 삭제 권한
- ADMIN: 모든 상태 삭제 가능
- CUSTOMER: PENDING만 취소 가능, 그 외 → ACCESS_DENIED

## 세션 자동 생성
- 주문 생성 시 활성 세션 없으면 자동 생성

## endSession 규칙
- PENDING/PREPARING 주문 존재 시 → HAS_INCOMPLETE_ORDERS 에러
- 완료 주문만 있을 때: JSON serialize → order_history, 원본 삭제, 세션 종료

## SSE 설정
- Heartbeat: 15초
- Timeout: 10분 (600,000ms)
- 이벤트: NEW_ORDER, ORDER_STATUS_CHANGED, ORDER_DELETED, TABLE_RESET
