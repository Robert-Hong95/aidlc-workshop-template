# Components - 테이블오더 서비스

## 아키텍처 개요
- **Backend**: Layered Architecture + DDD (Domain-Driven Design)
- **Frontend**: Turborepo 모노레포 (고객앱 + 관리자앱 + 공유 패키지)
- **API 구조**: 역할 기반 분리 (/api/customer/*, /api/admin/*)

---

## 1. Backend Components (Spring Boot)

### 1.1 Domain Layer

#### Store Domain
- **책임**: 매장 엔티티, 매장 비즈니스 규칙
- **엔티티**: Store

#### Table Domain
- **책임**: 테이블 엔티티, 테이블 세션 관리 규칙
- **엔티티**: StoreTable, TableSession

#### Menu Domain
- **책임**: 메뉴/카테고리 엔티티, 메뉴 검증 규칙
- **엔티티**: Menu, Category

#### Order Domain
- **책임**: 주문 엔티티, 주문 상태 전이 규칙
- **엔티티**: Order, OrderItem, OrderHistory

#### Auth Domain
- **책임**: 인증 관련 엔티티, 인증 규칙
- **엔티티**: Admin (매장 관리자 계정)

### 1.2 Service Layer

#### StoreService
- **책임**: 매장 CRUD 오케스트레이션

#### TableService
- **책임**: 테이블 관리, 세션 라이프사이클 오케스트레이션

#### MenuService
- **책임**: 메뉴/카테고리 CRUD 오케스트레이션

#### OrderService
- **책임**: 주문 생성, 상태 변경, 삭제 오케스트레이션, SSE 이벤트 발행

#### AuthService
- **책임**: 관리자/테이블 인증, JWT 토큰 관리

#### SseEmitterService
- **책임**: SSE 연결 관리, 이벤트 브로드캐스트

#### FileStorageService
- **책임**: 이미지 파일 업로드/조회

### 1.3 Repository Layer

#### StoreRepository, TableRepository, TableSessionRepository
#### MenuRepository, CategoryRepository
#### OrderRepository, OrderItemRepository, OrderHistoryRepository
#### AdminRepository

### 1.4 Controller Layer (API)

#### Customer API Controllers
- **TableAuthController**: /api/customer/auth/* - 테이블 인증
- **CustomerMenuController**: /api/customer/menus/* - 메뉴 조회
- **CustomerOrderController**: /api/customer/orders/* - 주문 생성/조회
- **CustomerSseController**: /api/customer/sse/* - 고객 SSE 연결

#### Admin API Controllers
- **AdminAuthController**: /api/admin/auth/* - 관리자 인증
- **AdminOrderController**: /api/admin/orders/* - 주문 모니터링/상태변경/삭제
- **AdminTableController**: /api/admin/tables/* - 테이블 관리
- **AdminMenuController**: /api/admin/menus/* - 메뉴 CRUD
- **AdminStoreController**: /api/admin/stores/* - 매장 관리
- **AdminSseController**: /api/admin/sse/* - 관리자 SSE 연결

#### Common Controllers
- **FileController**: /api/files/* - 이미지 업로드/서빙

### 1.5 Infrastructure Layer

#### Security
- **JwtTokenProvider**: JWT 토큰 생성/검증
- **SecurityConfig**: Spring Security 설정, 필터 체인

#### SSE
- **SseEmitterManager**: SSE 연결 풀 관리

#### File
- **LocalFileStorage**: 로컬 파일 시스템 저장 구현

---

## 2. Frontend Components (Turborepo 모노레포)

### 2.1 레이어 구조

각 앱(customer, admin)은 동일한 레이어 구조를 따릅니다:

```
apps/{app-name}/
├── app/                    # App Router (라우팅, 페이지, 레이아웃)
│   ├── layout.tsx          # Root Layout (Provider 래핑)
│   ├── (auth)/             # 인증 관련 라우트 그룹
│   └── (main)/             # 메인 콘텐츠 라우트 그룹
├── components/             # 앱 전용 UI 컴포넌트
│   ├── layout/             # 레이아웃 컴포넌트 (Header, Nav, Footer)
│   └── features/           # 기능별 컴포넌트 그룹
├── hooks/                  # 앱 전용 커스텀 훅
├── stores/                 # 클라이언트 상태 관리 (Zustand)
├── lib/                    # 앱 전용 유틸리티
├── types/                  # 앱 전용 타입 정의
└── styles/                 # 앱 전용 스타일 (Tailwind 확장)
```

### 2.2 공유 패키지 (packages/)

#### @table-order/ui
공유 UI 컴포넌트 라이브러리 (Tailwind CSS 기반)

```
packages/ui/
├── components/
│   ├── Button.tsx          # 버튼 (variant: primary/secondary/danger, size: sm/md/lg)
│   ├── Card.tsx            # 카드 컨테이너
│   ├── Modal.tsx           # 모달 다이얼로그 (확인 팝업 포함)
│   ├── Input.tsx           # 텍스트 입력 (label, error 표시)
│   ├── Select.tsx          # 셀렉트 드롭다운
│   ├── Badge.tsx           # 상태 뱃지 (color variant)
│   ├── Spinner.tsx         # 로딩 스피너
│   ├── Toast.tsx           # 토스트 알림 (성공/실패 피드백)
│   ├── EmptyState.tsx      # 빈 상태 안내 메시지
│   ├── Pagination.tsx      # 페이지네이션
│   ├── ImageUpload.tsx     # 이미지 업로드 (미리보기 포함)
│   └── ConfirmDialog.tsx   # 확인/취소 다이얼로그
├── layouts/
│   ├── PageLayout.tsx      # 페이지 공통 레이아웃
│   └── FormLayout.tsx      # 폼 공통 레이아웃
└── index.ts                # 배럴 export
```

#### @table-order/api-client
Backend API 호출 클라이언트 (fetch 기반)

```
packages/api-client/
├── client.ts               # 기본 HTTP 클라이언트 (인터셉터, 에러 핸들링)
├── auth.ts                 # 인증 API (login, token refresh)
├── stores.ts               # 매장 API
├── tables.ts               # 테이블 API
├── menus.ts                # 메뉴 API
├── orders.ts               # 주문 API
├── files.ts                # 파일 업로드 API
├── sse.ts                  # SSE 연결 헬퍼 (EventSource 래핑, 자동 재연결)
└── types/                  # API 요청/응답 타입 정의
    ├── auth.ts
    ├── store.ts
    ├── table.ts
    ├── menu.ts
    ├── order.ts
    └── common.ts           # 공통 타입 (ApiResponse, PageResponse, ErrorResponse)
```

#### @table-order/shared
공유 유틸리티, 상수, 타입

```
packages/shared/
├── utils/
│   ├── format.ts           # 포맷팅 (가격: ₩1,000, 날짜: YYYY-MM-DD HH:mm)
│   ├── validation.ts       # 공통 검증 (가격 범위, 필수 필드)
│   └── storage.ts          # localStorage 래퍼 (JSON 직렬화/역직렬화)
├── constants/
│   ├── order-status.ts     # 주문 상태 enum (PENDING, PREPARING, COMPLETED)
│   └── config.ts           # 공통 설정값 (API base URL, SSE 재연결 간격 등)
└── types/
    └── common.ts           # 공통 도메인 타입
```

### 2.3 고객용 앱 (apps/customer)

#### Pages (App Router)
| 경로 | 컴포넌트 | 설명 |
|------|---------|------|
| / | MenuPage | 카테고리별 메뉴 목록 (기본 화면) |
| /cart | CartPage | 장바구니 관리 |
| /order/confirm | OrderConfirmPage | 주문 최종 확인 → 주문 생성 |
| /orders | OrderListPage | 현재 세션 주문 내역 조회 |
| /setup | SetupPage | 테이블 초기 설정 (관리자 1회 수행) |

#### Feature Components
```
components/
├── layout/
│   ├── CustomerHeader.tsx      # 상단 헤더 (매장명, 테이블번호 표시)
│   ├── BottomNav.tsx           # 하단 네비게이션 (메뉴, 장바구니, 주문내역)
│   └── CartFloatingButton.tsx  # 장바구니 플로팅 버튼 (아이템 수 뱃지)
├── features/
│   ├── menu/
│   │   ├── CategoryTabs.tsx    # 카테고리 탭 네비게이션 (가로 스크롤)
│   │   ├── MenuCard.tsx        # 메뉴 카드 (이미지, 이름, 가격, 설명, 추가 버튼)
│   │   └── MenuGrid.tsx       # 메뉴 그리드 레이아웃
│   ├── cart/
│   │   ├── CartItem.tsx        # 장바구니 항목 (수량 조절, 삭제)
│   │   ├── CartSummary.tsx     # 장바구니 요약 (총 금액, 주문 버튼)
│   │   └── QuantityControl.tsx # 수량 증감 컨트롤 (+/- 버튼)
│   ├── order/
│   │   ├── OrderCard.tsx       # 주문 카드 (주문번호, 시각, 상태)
│   │   ├── OrderItemList.tsx   # 주문 메뉴 목록
│   │   ├── OrderStatusBadge.tsx # 주문 상태 뱃지 (대기중/준비중/완료)
│   │   └── OrderSuccessModal.tsx # 주문 성공 모달 (5초 후 자동 리다이렉트)
│   └── setup/
│       └── SetupForm.tsx       # 초기 설정 폼 (매장ID, 테이블번호, 비밀번호)
```

#### Hooks
| Hook | 책임 | 상태 관리 |
|------|------|----------|
| useCart | 장바구니 CRUD, 총 금액 계산 | Zustand + localStorage 동기화 |
| useTableAuth | 자동 로그인, 토큰 관리, 인증 상태 | Zustand + localStorage |
| useSSE | SSE 연결, 자동 재연결, 이벤트 핸들링 | EventSource 래핑 |
| useMenu | 메뉴/카테고리 데이터 fetch | React Query (TanStack Query) |
| useOrders | 주문 내역 fetch, 주문 생성 mutation | React Query |

#### Stores (Zustand)
```
stores/
├── cart-store.ts           # 장바구니 상태 (items, addItem, removeItem, updateQuantity, clear, total)
├── auth-store.ts           # 인증 상태 (token, storeId, tableId, isAuthenticated, login, logout)
└── order-event-store.ts    # SSE 이벤트 상태 (주문 상태 변경 실시간 반영)
```

### 2.4 관리자용 앱 (apps/admin)

#### Pages (App Router)
| 경로 | 컴포넌트 | 설명 |
|------|---------|------|
| /login | LoginPage | 관리자 로그인 |
| / | DashboardPage | 실시간 주문 모니터링 (그리드) |
| /tables | TableManagePage | 테이블 설정/세션 관리 |
| /menus | MenuManagePage | 메뉴 CRUD |
| /stores | StoreManagePage | 매장 관리 |

#### Feature Components
```
components/
├── layout/
│   ├── AdminHeader.tsx         # 상단 헤더 (매장명, 로그아웃)
│   ├── Sidebar.tsx             # 사이드바 네비게이션 (대시보드, 테이블, 메뉴, 매장)
│   └── AdminLayout.tsx         # 관리자 공통 레이아웃 (Header + Sidebar + Content)
├── features/
│   ├── dashboard/
│   │   ├── TableCardGrid.tsx   # 테이블 카드 그리드 레이아웃
│   │   ├── TableCard.tsx       # 테이블 카드 (테이블번호, 총 주문액, 최신 주문 미리보기)
│   │   ├── OrderPreview.tsx    # 주문 미리보기 (축약 메뉴 목록)
│   │   ├── OrderDetailModal.tsx # 주문 상세 모달 (전체 메뉴, 상태 변경 버튼)
│   │   ├── OrderStatusControl.tsx # 주문 상태 변경 컨트롤 (대기중→준비중→완료)
│   │   ├── NewOrderHighlight.tsx # 신규 주문 강조 애니메이션
│   │   └── TableFilter.tsx     # 테이블별 필터링
│   ├── table/
│   │   ├── TableSetupForm.tsx  # 테이블 초기 설정 폼
│   │   ├── TableSessionControl.tsx # 세션 관리 (이용 완료 버튼)
│   │   ├── OrderDeleteButton.tsx # 주문 삭제 버튼 (확인 팝업 연동)
│   │   └── OrderHistoryModal.tsx # 과거 주문 내역 모달 (날짜 필터링)
│   ├── menu/
│   │   ├── MenuForm.tsx        # 메뉴 등록/수정 폼 (이미지 업로드 포함)
│   │   ├── MenuList.tsx        # 메뉴 목록 (카테고리별)
│   │   ├── MenuItemRow.tsx     # 메뉴 항목 행 (수정/삭제 버튼)
│   │   ├── CategoryManager.tsx # 카테고리 관리
│   │   └── MenuOrderDrag.tsx   # 메뉴 노출 순서 드래그 정렬
│   └── store/
│       ├── StoreForm.tsx       # 매장 등록 폼
│       └── StoreList.tsx       # 매장 목록
```

#### Hooks
| Hook | 책임 | 상태 관리 |
|------|------|----------|
| useAdminAuth | 로그인, 로그아웃, 16시간 세션 관리 | Zustand + localStorage |
| useSSE | SSE 연결, 신규 주문/상태 변경 이벤트 수신 | EventSource 래핑 |
| useDashboard | 대시보드 데이터 (테이블별 주문 집계) | React Query + SSE 이벤트 머지 |
| useTables | 테이블 CRUD, 세션 관리 mutation | React Query |
| useMenus | 메뉴 CRUD mutation | React Query |
| useStores | 매장 CRUD mutation | React Query |
| useOrderHistory | 과거 주문 내역 fetch (날짜 필터) | React Query |

#### Stores (Zustand)
```
stores/
├── auth-store.ts           # 인증 상태 (token, storeId, isAuthenticated, login, logout)
└── dashboard-store.ts      # 대시보드 실시간 상태 (테이블별 주문 맵, SSE 이벤트 반영)
```

### 2.5 공통 기술 스택 및 설정

| 영역 | 기술 | 용도 |
|------|------|------|
| 프레임워크 | Next.js 15 (App Router) | SSR/CSR, 라우팅 |
| 스타일링 | Tailwind CSS 4 | 유틸리티 기반 스타일링 |
| 상태 관리 | Zustand | 클라이언트 전역 상태 |
| 서버 상태 | TanStack Query (React Query) | API 데이터 캐싱, mutation |
| 폼 관리 | React Hook Form + Zod | 폼 검증 |
| 모노레포 | Turborepo | 빌드 캐싱, 태스크 오케스트레이션 |
| 패키지 매니저 | pnpm | 워크스페이스 관리 |
| 드래그 정렬 | @dnd-kit | 메뉴 순서 드래그 |
