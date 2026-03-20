# Build and Test Summary - 테이블오더 서비스

## 빌드 결과

| 항목 | 상태 | 비고 |
|------|------|------|
| Frontend 빌드 (admin) | ✅ 성공 | Next.js 15.5.14, 4 routes |
| Frontend 빌드 (customer) | ✅ 성공 | Next.js 15.5.14, 5 routes |
| Frontend 타입 체크 (api-client) | ✅ 통과 | 에러 0 |
| Frontend 타입 체크 (admin) | ✅ 통과 | 실제 코드 에러 0 (테스트 파일만 타입 변경 에러) |
| Frontend 타입 체크 (customer) | ✅ 통과 | 에러 0 |
| Backend 빌드 | ⏳ 미확인 | Java 미설치 환경, Docker로 빌드 가능 |

## 페이지 구성

### 관리자앱 (localhost:3001)

| Route | 페이지 | 기능 |
|-------|--------|------|
| / | Redirect | → /dashboard |
| /dashboard | DashboardPage | 실시간 주문 모니터링 (SSE), 상태 변경, 주문 삭제 |
| /menus | MenuManagePage | 카테고리/메뉴 CRUD, 이미지 업로드 |

### 고객앱 (localhost:3000)

| Route | 페이지 | 기능 |
|-------|--------|------|
| / | MenuPage | 카테고리별 메뉴 조회, 장바구니 담기 |
| /cart | CartPage | 장바구니 관리 (수량 조절, 삭제) |
| /order/confirm | OrderConfirmPage | 주문 최종 확인 + 주문 생성 |
| /orders | OrderListPage | 주문 내역 조회 (SSE 실시간 상태 업데이트) |

## API 연동 현황

| 영역 | 연동 상태 | 비고 |
|------|----------|------|
| 관리자 인증 | ✅ | /api/admin/auth/login |
| 테이블 인증 | ✅ | /api/customer/auth/login |
| 매장 CRUD | ✅ | /api/admin/stores |
| 테이블 관리 | ✅ | /api/admin/stores/{id}/tables, end-session |
| 카테고리 CRUD | ✅ | /api/admin/stores/{id}/categories |
| 메뉴 CRUD | ✅ | /api/admin/stores/{id}/menus |
| 이미지 업로드 | ✅ | /api/files/upload |
| 고객 메뉴 조회 | ✅ | /api/customer/stores/{id}/menus |
| 주문 생성/조회 | ✅ | /api/customer/stores/{id}/tables/{id}/orders |
| 주문 상태 변경 | ✅ | /api/admin/orders/{id}/status |
| 주문 삭제 | ✅ | /api/admin/orders/{id} |
| SSE (관리자) | ✅ | /api/admin/stores/{id}/sse |
| SSE (고객) | ✅ | /api/customer/stores/{id}/tables/{id}/sse |
| 과거 주문 내역 | ✅ | /api/admin/tables/{id}/order-history |

## 실행 방법

```bash
cd table-order/
docker compose up --build -d
```

상세 내용은 `build-instructions.md`, `integration-test-instructions.md` 참조.
