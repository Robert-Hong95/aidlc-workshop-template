# Unit of Work - 테이블오더 서비스

**개발 방식**: Unit별 Full-Stack (Backend + Frontend 함께)
**분해 기준**: 도메인별 분리

---

## Unit 0: 프로젝트 초기 설정 (Foundation)

### 책임
- Spring Boot 프로젝트 초기 구조 (Layered + DDD 패키지 구조)
- Turborepo 모노레포 초기 구조 (apps/customer, apps/admin, packages/*)
- MySQL 스키마 초기 설정
- Docker Compose 구성 (Backend, Frontend x2, MySQL)
- 공유 패키지 기본 구조 (@table-order/ui, api-client, shared)
- Spring Security + JWT 기본 설정
- 공통 에러 핸들링, API 응답 포맷

### 포함 범위
- 프로젝트 스캐폴딩 (코드 생성 기반)
- 빌드/실행 환경 구성
- 공통 인프라 코드

### 산출물
- Backend: Spring Boot 프로젝트 구조, 공통 설정, Security 설정
- Frontend: Turborepo 구조, 공유 패키지 기본 코드, Tailwind/TanStack Query 설정
- Infra: Docker Compose, MySQL init script

---

## Unit 1: 인증 (Auth)

### 책임
- 관리자 로그인/로그아웃 (JWT 16시간 세션)
- 테이블 태블릿 인증 (자동 로그인)
- QR 코드 모바일 웹 접근

### Backend
- Domain: Admin 엔티티
- Service: AuthService
- Controller: AdminAuthController, TableAuthController
- Infrastructure: JwtTokenProvider

### Frontend
- 관리자앱: LoginPage, useAdminAuth hook, auth-store
- 고객앱: SetupPage, useTableAuth hook, auth-store

### Stories
- US-A01 (매장 인증), US-C01 (테이블 자동 로그인)

---

## Unit 2: 매장/테이블 관리 (Store & Table)

### 책임
- 매장 등록/조회 (다중 매장)
- 테이블 초기 설정
- 테이블 세션 라이프사이클 (시작/종료)
- 과거 주문 내역 조회

### Backend
- Domain: Store, StoreTable, TableSession 엔티티
- Service: StoreService, TableService
- Controller: AdminStoreController, AdminTableController
- Repository: StoreRepository, TableRepository, TableSessionRepository

### Frontend
- 관리자앱: StoreManagePage, TableManagePage, StoreForm, StoreList, TableSetupForm, TableSessionControl, OrderHistoryModal

### Stories
- US-A05 (매장 관리), US-A03 (테이블 관리)

---

## Unit 3: 메뉴 관리 (Menu)

### 책임
- 카테고리/메뉴 CRUD
- 메뉴 노출 순서 관리
- 이미지 업로드
- 고객용 메뉴 조회

### Backend
- Domain: Menu, Category 엔티티
- Service: MenuService, FileStorageService
- Controller: AdminMenuController, CustomerMenuController, FileController
- Infrastructure: LocalFileStorage

### Frontend
- 관리자앱: MenuManagePage, MenuForm, MenuList, MenuItemRow, CategoryManager, MenuOrderDrag
- 고객앱: MenuPage, CategoryTabs, MenuCard, MenuGrid

### Stories
- US-A04 (메뉴 관리), US-C02 (메뉴 조회), US-S01 (이미지 업로드)

---

## Unit 4: 주문 (Order)

### 책임
- 장바구니 관리 (클라이언트)
- 주문 생성
- 주문 상태 변경/삭제
- 주문 내역 조회 (고객)
- 실시간 주문 모니터링 (관리자)
- SSE 실시간 통신

### Backend
- Domain: Order, OrderItem, OrderHistory 엔티티
- Service: OrderService, SseEmitterService
- Controller: CustomerOrderController, AdminOrderController, CustomerSseController, AdminSseController
- Infrastructure: SseEmitterManager

### Frontend
- 고객앱: CartPage, OrderConfirmPage, OrderListPage, CartItem, CartSummary, QuantityControl, OrderCard, OrderItemList, OrderStatusBadge, OrderSuccessModal, CartFloatingButton, useCart, useOrders, useSSE, cart-store, order-event-store
- 관리자앱: DashboardPage, TableCardGrid, TableCard, OrderPreview, OrderDetailModal, OrderStatusControl, NewOrderHighlight, TableFilter, OrderDeleteButton, useSSE, useDashboard, dashboard-store

### Stories
- US-C03 (장바구니), US-C04 (주문 생성), US-C05 (주문 내역), US-A02 (실시간 모니터링), US-S02 (SSE 실시간 통신)

---

## 코드 조직 구조 (Greenfield)

```
table-order/                          # Workspace Root
├── backend/                          # Spring Boot
│   └── src/main/java/com/tableorder/
│       ├── common/                   # 공통 (에러핸들링, 응답포맷)
│       ├── config/                   # Security, SSE 설정
│       ├── auth/                     # Unit 1: 인증 도메인
│       │   ├── domain/
│       │   ├── service/
│       │   ├── controller/
│       │   └── repository/
│       ├── store/                    # Unit 2: 매장/테이블 도메인
│       │   ├── domain/
│       │   ├── service/
│       │   ├── controller/
│       │   └── repository/
│       ├── menu/                     # Unit 3: 메뉴 도메인
│       │   ├── domain/
│       │   ├── service/
│       │   ├── controller/
│       │   └── repository/
│       └── order/                    # Unit 4: 주문 도메인
│           ├── domain/
│           ├── service/
│           ├── controller/
│           └── repository/
├── frontend/                         # Turborepo 모노레포
│   ├── apps/
│   │   ├── customer/                 # 고객앱
│   │   └── admin/                    # 관리자앱
│   ├── packages/
│   │   ├── ui/                       # 공유 UI
│   │   ├── api-client/               # API 클라이언트
│   │   └── shared/                   # 공유 유틸
│   ├── turbo.json
│   ├── pnpm-workspace.yaml
│   └── package.json
├── docker-compose.yml
└── aidlc-docs/
```
