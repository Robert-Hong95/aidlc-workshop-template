# Integration Test Instructions

## Purpose
Unit 간 상호작용을 검증하여 전체 시스템이 올바르게 동작하는지 확인합니다.

## 사전 준비

### 1. MySQL 실행 및 스키마 초기화
```bash
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS table_order;"
mysql -u root -p table_order < table-order/backend/src/main/resources/db/init.sql
```

### 2. 애플리케이션 실행
```bash
cd table-order/backend
./gradlew bootRun
```

## Integration Test Scenarios

### Scenario 1: Auth → Store → Table 흐름
1. `POST /api/stores` — 매장+관리자 생성
2. `POST /api/admin/auth/login` — 관리자 로그인 → JWT 획득
3. `POST /api/admin/stores/{storeId}/tables` — 테이블 생성 (JWT 필요)
4. **검증**: 매장 생성 → 로그인 → 테이블 관리 전체 흐름

### Scenario 2: Menu 관리 흐름
1. 관리자 로그인 (Scenario 1의 JWT 사용)
2. `POST /api/admin/categories` — 카테고리 생성
3. `POST /api/admin/menus` — 메뉴 생성
4. `GET /api/customer/stores/{storeId}/menus` — 고객 메뉴 조회 (인증 불필요)
5. **검증**: 관리자가 등록한 메뉴가 고객에게 노출

### Scenario 3: 주문 전체 흐름 (핵심)
1. 관리자: 매장+테이블+메뉴 세팅 (Scenario 1+2)
2. 고객: `POST /api/customer/auth/qr-login` — QR 로그인
3. 고객: `POST /api/customer/stores/{storeId}/tables/{tableId}/orders` — 주문 생성
4. **검증**: 테이블 세션 자동 시작, 주문 상태 PENDING
5. 관리자: `GET /api/admin/stores/{storeId}/orders` — 주문 목록 확인
6. 관리자: `PUT /api/admin/orders/{orderId}/status` — PENDING→CONFIRMED→PREPARING→COMPLETED
7. **검증**: 각 상태 전이 성공

### Scenario 4: SSE 실시간 알림
1. 관리자: `GET /api/admin/sse/subscribe?storeId={id}&adminId={id}` — SSE 구독
2. 고객: 주문 생성
3. **검증**: 관리자 SSE로 NEW_ORDER 이벤트 수신
4. 관리자: 주문 상태 변경
5. **검증**: 고객 SSE로 STATUS_CHANGED 이벤트 수신

### Scenario 5: 주문 삭제
1. 고객: PENDING 주문 삭제 → 성공
2. 고객: CONFIRMED 주문 삭제 → 실패 (INVALID_ORDER_STATUS)
3. 관리자: 모든 상태 주문 삭제 → 성공
4. **검증**: 권한 및 상태 제약 동작

### Scenario 6: Rate Limiting
1. `POST /api/stores` 11회 연속 호출
2. **검증**: 11번째 요청에서 429 RATE_LIMIT_EXCEEDED

## 수동 테스트 도구
```bash
# curl 예시 — 매장 생성
curl -X POST http://localhost:8080/api/stores \
  -H "Content-Type: application/json" \
  -d '{"storeCode":"STORE01","storeName":"테스트매장","username":"admin","password":"Test1234!"}'

# curl 예시 — 로그인
curl -X POST http://localhost:8080/api/admin/auth/login \
  -H "Content-Type: application/json" \
  -d '{"storeCode":"STORE01","username":"admin","password":"Test1234!"}'

# SSE 구독 (터미널에서 실시간 수신)
curl -N http://localhost:8080/api/admin/sse/subscribe?storeId=1\&adminId=1
```
