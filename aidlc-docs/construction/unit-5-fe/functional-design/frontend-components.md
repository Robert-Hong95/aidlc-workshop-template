# Frontend Components - Unit 5-FE (공통 레이아웃 + Auth UI)

## 1. 공유 패키지 (packages/)

### 1.1 @table-order/ui

#### Button
- **Props**: `variant: 'primary' | 'secondary' | 'danger'`, `size: 'sm' | 'md' | 'lg'`, `disabled`, `loading`, `children`, `onClick`, `type`, `data-testid`
- **동작**: loading=true 시 Spinner 표시 + 클릭 비활성화

#### Card
- **Props**: `children`, `className`, `onClick`, `data-testid`

#### Input
- **Props**: `label`, `type`, `error`, `placeholder`, `value`, `onChange`, `disabled`, `data-testid`
- **동작**: error 존재 시 빨간 테두리 + 에러 메시지 표시

#### Modal
- **Props**: `isOpen`, `onClose`, `title`, `children`, `data-testid`
- **동작**: 배경 클릭 시 닫기, ESC 키 닫기

#### ConfirmDialog
- **Props**: `isOpen`, `onConfirm`, `onCancel`, `title`, `message`, `confirmText`, `cancelText`, `variant`

#### Toast
- **Props**: `message`, `type: 'success' | 'error' | 'info'`, `duration`
- **동작**: duration(기본 3초) 후 자동 사라짐
- **전역 관리**: `useToast()` hook으로 어디서든 호출

#### Spinner
- **Props**: `size: 'sm' | 'md' | 'lg'`

#### EmptyState
- **Props**: `message`, `icon`, `action` (버튼 텍스트 + onClick)

#### Badge
- **Props**: `children`, `color: 'gray' | 'yellow' | 'blue' | 'green' | 'red'`

### 1.2 @table-order/api-client

#### client.ts (HTTP 클라이언트)
- `createApiClient(config: { baseUrl, getToken, onUnauthorized })` → 인스턴스 반환
- 요청 인터셉터: Authorization 헤더 자동 추가
- 응답 인터셉터: 401 시 토큰 갱신 + 재시도, 동시성 큐잉
- 메서드: `get<T>()`, `post<T>()`, `put<T>()`, `delete<T>()`

#### auth.ts (인증 API)
- `adminLogin(storeCode, username, password)` → `TokenResponse`
- `tableLogin(storeCode, tableNo, password)` → `TokenResponse`
- `refreshToken(refreshToken)` → `TokenResponse`
- `verifyTablePassword(password)` → `{ valid: boolean }`

#### types/auth.ts
```typescript
interface LoginRequest { storeCode: string; username: string; password: string; }
interface TableLoginRequest { storeCode: string; tableNo: number; password: string; }
interface TokenResponse { accessToken: string; refreshToken: string; storeId: number; storeName: string; }
interface AdminTokenResponse extends TokenResponse { adminId: number; username: string; }
interface TableTokenResponse extends TokenResponse { tableId: number; tableNo: number; }
```

#### types/common.ts
```typescript
interface ApiResponse<T> { success: boolean; data: T; message?: string; }
interface ErrorResponse { success: boolean; message: string; code: string; }
```

### 1.3 @table-order/shared

#### constants/order-status.ts
```typescript
enum OrderStatus { PENDING = 'PENDING', PREPARING = 'PREPARING', COMPLETED = 'COMPLETED' }
```

#### constants/config.ts
```typescript
const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';
const SSE_RECONNECT_INTERVAL = 3000;
const TOAST_DURATION = 3000;
```

#### utils/format.ts
- `formatPrice(price: number)` → `"₩1,000"`
- `formatDateTime(date: string)` → `"2026-03-20 13:00"`

#### utils/storage.ts
- `getItem<T>(key)`, `setItem(key, value)`, `removeItem(key)` — JSON 직렬화/역직렬화 래퍼

---

## 2. 관리자앱 컴포넌트

### 2.1 LoginPage (`/login`)
- **구성**: 로고 + 로그인 폼 (매장코드, 사용자명, 비밀번호) + 제출 버튼
- **상태**: `useAdminAuth` hook 사용
- **검증**: react-hook-form + zod 스키마
- **로그인 시도 제한**: 실패 횟수에 따른 카운트다운 타이머 표시
- **로딩**: 제출 중 버튼 로딩 상태

### 2.2 AdminLayout
- **구성**: AdminHeader (상단) + Sidebar (좌측) + Content (우측)
- **반응형**: 
  - 데스크톱(≥1024px): 사이드바 표시 (토글로 접기/펼치기)
  - 모바일(<1024px): 사이드바 숨김, 햄버거 메뉴로 드로어 열기
- **사이드바 상태**: localStorage에 접힘/펼침 상태 저장

### 2.3 AdminHeader
- **구성**: 햄버거 메뉴 버튼 (모바일) | 매장명 표시 | 로그아웃 버튼
- **동작**: 로그아웃 클릭 → 토큰 삭제 → `/login` 이동

### 2.4 Sidebar
- **메뉴 항목**: 대시보드(`/`), 테이블 관리(`/tables`), 메뉴 관리(`/menus`), 매장 관리(`/stores`)
- **접힘 모드**: 아이콘만 표시
- **펼침 모드**: 아이콘 + 텍스트 표시
- **활성 메뉴**: 현재 경로에 따라 하이라이트

### 2.5 auth-store (Zustand)
```typescript
interface AdminAuthState {
  accessToken: string | null;
  refreshToken: string | null;
  storeId: number | null;
  storeName: string | null;
  adminId: number | null;
  username: string | null;
  isAuthenticated: boolean;
  login: (response: AdminTokenResponse) => void;
  logout: () => void;
  setTokens: (access: string, refresh: string) => void;
  hydrate: () => void; // localStorage에서 복원
}
```

### 2.6 useAdminAuth hook
- `login(storeCode, username, password)` → API 호출 + store 업데이트
- `logout()` → store 초기화 + localStorage 삭제
- `isAuthenticated` → 인증 상태
- `loginAttempts` → 현재 실패 횟수
- `lockoutRemaining` → 남은 대기 시간 (초)

---

## 3. 고객앱 컴포넌트

### 3.1 SetupPage (`/setup`)
- **구성**: 로고 + 설정 폼 (매장코드, 테이블번호, 비밀번호) + 설정 완료 버튼
- **상태**: `useTableAuth` hook 사용
- **검증**: 매장코드 필수, 테이블번호 숫자/1이상, 비밀번호 필수

### 3.2 CustomerHeader
- **구성**: 매장명 | 테이블번호 뱃지 | 설정(⚙️) 아이콘
- **설정 아이콘**: 클릭 시 비밀번호 확인 모달 → 성공 시 `/setup` 이동

### 3.3 BottomNav
- **탭 구성**: 메뉴(`/`) | 장바구니(`/cart`) | 주문내역(`/orders`)
- **활성 탭**: 현재 경로에 따라 하이라이트
- **장바구니 탭**: 아이템 수 뱃지 표시 (cart-store 연동)
- **터치 영역**: 최소 44x44px

### 3.4 CustomerLayout
- **구성**: CustomerHeader (상단) + Content (중앙) + BottomNav (하단)
- **Content 영역**: 상단/하단 고정 영역 제외한 스크롤 가능 영역

### 3.5 PasswordConfirmModal
- **구성**: "설정 변경을 위해 비밀번호를 입력해주세요" + 비밀번호 입력 + 확인/취소 버튼
- **동작**: 서버 검증 API 호출 → 성공 시 토큰 삭제 + `/setup` 이동

### 3.6 auth-store (Zustand)
```typescript
interface TableAuthState {
  accessToken: string | null;
  refreshToken: string | null;
  storeId: number | null;
  storeName: string | null;
  storeCode: string | null;
  tableId: number | null;
  tableNo: number | null;
  isAuthenticated: boolean;
  login: (response: TableTokenResponse, storeCode: string) => void;
  logout: () => void;
  setTokens: (access: string, refresh: string) => void;
  hydrate: () => void;
}
```

### 3.7 useTableAuth hook
- `login(storeCode, tableNo, password)` → API 호출 + store 업데이트
- `logout()` → store 초기화 + localStorage 삭제
- `verifyPassword(password)` → 비밀번호 확인 API 호출
- `isAuthenticated` → 인증 상태
- `autoLogin()` → 앱 시작 시 토큰 복원 + 갱신 시도

---

## 4. 색상 테마

### 4.1 고객앱 (AWS 스타일)
| 용도 | 색상 | 코드 |
|------|------|------|
| Primary (액센트) | 오렌지 | `#FF9900` |
| Primary Hover | 다크 오렌지 | `#EC7211` |
| Text Primary | 다크 네이비 | `#232F3E` |
| Text Secondary | 그레이 | `#687078` |
| Background | 화이트 | `#FFFFFF` |
| Surface | 라이트 그레이 | `#F2F3F3` |
| Border | 그레이 | `#D5DBDB` |
| Error | 레드 | `#D13212` |
| Success | 그린 | `#1D8102` |

### 4.2 관리자앱 (다크 사이드바 + 라이트 콘텐츠)
| 용도 | 색상 | 코드 |
|------|------|------|
| Sidebar Background | 다크 네이비 | `#232F3E` |
| Sidebar Text | 화이트 | `#FFFFFF` |
| Sidebar Active | 오렌지 | `#FF9900` |
| Header Background | 화이트 | `#FFFFFF` |
| Content Background | 라이트 그레이 | `#F2F3F3` |
| Primary (액센트) | 오렌지 | `#FF9900` |
| Text Primary | 다크 | `#16191F` |
| Text Secondary | 그레이 | `#687078` |
