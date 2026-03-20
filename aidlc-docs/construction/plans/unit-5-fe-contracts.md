# Contract/Interface Definition - Unit 5-FE (공통 레이아웃 + Auth UI)

## Unit Context
- **Stories**: US-A01 (매장 인증), US-C01 (테이블 자동 로그인)
- **Dependencies**: Backend Auth API (미완성 - 타입 정의 기반 개발)
- **작업 디렉토리**: `table-order/frontend/`

---

## 1. Shared Packages

### @table-order/shared

#### utils/storage.ts
- `getItem<T>(key: string): T | null` — localStorage에서 JSON 파싱하여 반환
- `setItem<T>(key: string, value: T): void` — JSON 직렬화하여 localStorage 저장
- `removeItem(key: string): void` — localStorage에서 삭제

#### utils/format.ts
- `formatPrice(price: number): string` — 가격 포맷 (₩1,000)
- `formatDateTime(date: string): string` — 날짜 포맷 (YYYY-MM-DD HH:mm)

#### utils/validation.ts
- `isNotEmpty(value: string): boolean` — 빈 문자열/공백 체크
- `isPositiveNumber(value: number): boolean` — 양수 체크

### @table-order/api-client

#### client.ts
- `createApiClient(config: ApiClientConfig): ApiClient` — HTTP 클라이언트 인스턴스 생성
  - Config: `{ baseUrl, getToken, onUnauthorized, onRefreshToken }`
  - Returns: `{ get, post, put, delete }` 메서드를 가진 객체
  - 요청 인터셉터: Authorization 헤더 자동 추가
  - 응답 인터셉터: 401 시 토큰 갱신 + 재시도 (동시성 큐잉)

#### auth.ts
- `adminLogin(client, request: LoginRequest): Promise<AdminTokenResponse>` — 관리자 로그인
- `tableLogin(client, request: TableLoginRequest): Promise<TableTokenResponse>` — 테이블 로그인
- `refreshToken(client, token: string): Promise<TokenResponse>` — 토큰 갱신
- `verifyTablePassword(client, password: string): Promise<{ valid: boolean }>` — 테이블 비밀번호 확인

### @table-order/ui

#### Button
- Props: `variant`, `size`, `disabled`, `loading`, `children`, `onClick`, `type`, `data-testid`
- Renders: 버튼 요소, loading 시 Spinner 표시

#### Input
- Props: `label`, `type`, `error`, `placeholder`, `value`, `onChange`, `disabled`, `data-testid`
- Renders: label + input + error message

#### Modal
- Props: `isOpen`, `onClose`, `title`, `children`, `data-testid`
- Renders: 오버레이 + 모달 컨텐츠, ESC/배경 클릭 닫기

#### Toast (useToast hook)
- `useToast()`: `{ toast: (msg, type) => void }` — 토스트 표시
- ToastProvider: 앱 루트에 래핑

#### Spinner
- Props: `size`
- Renders: 로딩 스피너 애니메이션

#### EmptyState
- Props: `message`, `action`
- Renders: 빈 상태 메시지 + 선택적 액션 버튼

#### Badge
- Props: `children`, `color`
- Renders: 색상 뱃지

#### ConfirmDialog
- Props: `isOpen`, `onConfirm`, `onCancel`, `title`, `message`, `confirmText`, `cancelText`

---

## 2. 관리자앱 (apps/admin)

### stores/auth-store.ts
- `useAdminAuthStore()` — Zustand store
  - State: `accessToken`, `refreshToken`, `storeId`, `storeName`, `adminId`, `username`, `isAuthenticated`
  - Actions: `login(response)`, `logout()`, `setTokens(access, refresh)`, `hydrate()`

### hooks/useAdminAuth.ts
- `useAdminAuth()` — 관리자 인증 hook
  - Returns: `{ login, logout, isAuthenticated, isLoading, error, loginAttempts, lockoutRemaining }`
  - `login(storeCode, username, password)`: API 호출 + store 업데이트 + 시도 제한 관리
  - `logout()`: store 초기화 + localStorage 삭제

### hooks/useLoginLockout.ts
- `useLoginLockout()` — 점진적 로그인 시도 제한 hook
  - Returns: `{ attempts, isLocked, remainingSeconds, recordFailure, resetAttempts }`
  - 점진적 지연: 6회:1분, 7회:5분, 8회:15분, 9회+:60분

### components/layout/AdminHeader.tsx
- Props: `storeName`, `onMenuToggle`, `onLogout`
- Renders: 햄버거 버튼(모바일) + 매장명 + 로그아웃 버튼

### components/layout/Sidebar.tsx
- Props: `collapsed`, `onToggle`, `currentPath`
- Renders: 네비게이션 메뉴 (대시보드, 테이블, 메뉴, 매장), 접힘/펼침 토글

### components/layout/AdminLayout.tsx
- Props: `children`
- Renders: AdminHeader + Sidebar + Content 영역, 반응형 처리

### app/login/page.tsx (LoginPage)
- 로그인 폼 (매장코드, 사용자명, 비밀번호)
- react-hook-form + zod 검증
- 점진적 시도 제한 UI

---

## 3. 고객앱 (apps/customer)

### stores/auth-store.ts
- `useTableAuthStore()` — Zustand store
  - State: `accessToken`, `refreshToken`, `storeId`, `storeName`, `storeCode`, `tableId`, `tableNo`, `isAuthenticated`
  - Actions: `login(response, storeCode)`, `logout()`, `setTokens(access, refresh)`, `hydrate()`

### hooks/useTableAuth.ts
- `useTableAuth()` — 테이블 인증 hook
  - Returns: `{ login, logout, verifyPassword, isAuthenticated, isLoading, error, autoLogin }`
  - `login(storeCode, tableNo, password)`: API 호출 + store 업데이트
  - `autoLogin()`: 토큰 복원 + 갱신 시도
  - `verifyPassword(password)`: 비밀번호 확인 API 호출

### components/layout/CustomerHeader.tsx
- Props: `storeName`, `tableNo`, `onSettingsClick`
- Renders: 매장명 + 테이블번호 뱃지 + 설정(⚙️) 아이콘

### components/layout/BottomNav.tsx
- Props: `currentPath`, `cartItemCount`
- Renders: 3탭 (메뉴, 장바구니(뱃지), 주문내역)

### components/layout/CustomerLayout.tsx
- Props: `children`
- Renders: CustomerHeader + Content + BottomNav

### components/features/setup/SetupForm.tsx
- Props: `onSubmit`, `isLoading`, `error`
- Renders: 매장코드 + 테이블번호 + 비밀번호 입력 폼

### components/features/setup/PasswordConfirmModal.tsx
- Props: `isOpen`, `onConfirm`, `onCancel`, `isLoading`, `error`
- Renders: 비밀번호 입력 모달

### app/setup/page.tsx (SetupPage)
- 초기 설정 폼 + useTableAuth 연동
