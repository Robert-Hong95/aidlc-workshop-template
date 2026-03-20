# Integration Test Instructions

## Purpose
Unit 간 상호작용을 Docker Compose 환경에서 수동 검증.

## 환경 준비
```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
cd table-order/backend
docker-compose up -d
./gradlew bootRun
```

## 시나리오 1: 관리자 인증 → 매장/메뉴 관리
```bash
# 1. 관리자 로그인
curl -X POST http://localhost:8080/api/admin/auth/login \
  -H "Content-Type: application/json" \
  -d '{"storeCode":"STORE01","username":"admin","password":"pass1234"}'
# → JWT 토큰 획득

# 2. 카테고리 생성 (TOKEN 대체)
curl -X POST http://localhost:8080/api/admin/stores/1/categories \
  -H "Authorization: Bearer TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name":"음료"}'

# 3. 메뉴 생성
curl -X POST http://localhost:8080/api/admin/stores/1/menus \
  -H "Authorization: Bearer TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"categoryId":1,"name":"아메리카노","price":4500}'
```

## 시나리오 2: 고객 주문 플로우
```bash
# 1. 테이블 로그인
curl -X POST http://localhost:8080/api/customer/auth/login \
  -H "Content-Type: application/json" \
  -d '{"storeCode":"STORE01","tableNo":1,"password":"pass1234"}'

# 2. 메뉴 조회
curl http://localhost:8080/api/customer/stores/1/menus \
  -H "Authorization: Bearer TOKEN"

# 3. 주문 생성
curl -X POST http://localhost:8080/api/customer/stores/1/tables/1/orders \
  -H "Authorization: Bearer TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"items":[{"menuId":1,"quantity":2}]}'

# 4. 주문 조회
curl http://localhost:8080/api/customer/stores/1/tables/1/orders \
  -H "Authorization: Bearer TOKEN"
```

## 시나리오 3: SSE 실시간 + 주문 상태 변경
```bash
# 터미널 1: 관리자 SSE 구독
curl -N http://localhost:8080/api/admin/stores/1/sse \
  -H "Authorization: Bearer ADMIN_TOKEN"

# 터미널 2: 고객 주문 생성 → 터미널 1에서 NEW_ORDER 이벤트 확인

# 터미널 3: 관리자 상태 변경
curl -X PUT http://localhost:8080/api/admin/orders/1/status \
  -H "Authorization: Bearer ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"status":"PREPARING"}'
```

## 시나리오 4: 세션 종료 + 주문 이관
```bash
# 1. 주문 상태를 COMPLETED로 변경
curl -X PUT http://localhost:8080/api/admin/orders/1/status \
  -H "Authorization: Bearer ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"status":"COMPLETED"}'

# 2. 세션 종료
curl -X POST http://localhost:8080/api/admin/tables/1/end-session \
  -H "Authorization: Bearer ADMIN_TOKEN"

# 3. 과거 내역 확인
curl "http://localhost:8080/api/admin/tables/1/order-history?dateFrom=2026-03-01&dateTo=2026-03-31" \
  -H "Authorization: Bearer ADMIN_TOKEN"
```

## Cleanup
```bash
docker-compose down
```
