# Code Summary - Unit 5-FE (공통 레이아웃 + Auth UI)

## 생성 완료일: 2026-03-20

## 테스트 결과
- **총 60개 테스트, 5개 패키지, ALL PASSED**
- @table-order/shared: 12 tests (storage 4, format 3, validation 5)
- @table-order/api-client: 8 tests (client 4, auth 4)
- @table-order/ui: 10 tests (Spinner 1, Badge 1, Button 3, Input 2, Modal 3)
- @table-order/admin: 14 tests (auth-store 3, lockout 4, layout 7)
- @table-order/customer: 16 tests (auth-store 3, layout 7, setup 6)

## 생성된 파일 목록

### packages/shared/
| 파일 | 설명 |
|------|------|
| package.json | 패키지 설정 (ESM) |
| tsconfig.json, vitest.config.ts | 빌드/테스트 설정 |
| src/index.ts | 배럴 export |
| src/utils/storage.ts | localStorage 래퍼 (getItem, setItem, removeItem) |
| src/utils/format.ts | formatPrice, formatDateTime |
| src/utils/validation.ts | isNotEmpty, isPositiveNumber |
| __tests__/storage.test.ts | TC-5FE-001~004 |
| __tests__/format.test.ts | TC-5FE-005~007 |
| __tests__/validation.test.ts | TC-5FE-008~012 |

### packages/api-client/
| 파일 | 설명 |
|------|------|
| src/types/common.ts | ApiClient, ApiClientConfig 인터페이스 |
| src/types/auth.ts | LoginRequest, TokenResponse 등 타입 |
| src/client.ts | createApiClient — 인터셉터, 401 토큰 갱신, 동시성 큐 |
| src/auth.ts | adminLogin, tableLogin, refreshToken, verifyTablePassword |
| __tests__/client.test.ts | TC-5FE-013~016 |
| __tests__/auth.test.ts | auth API 함수 4개 테스트 |

### packages/ui/
| 파일 | 설명 |
|------|------|
| src/components/Spinner.tsx | 로딩 스피너 |
| src/components/Badge.tsx | 색상 뱃지 |
| src/components/Button.tsx | variant/size/loading 지원 버튼 |
| src/components/Input.tsx | label/error 지원 입력 필드 |
| src/components/Modal.tsx | ESC/오버레이 닫기 모달 |
| src/components/Toast.tsx | ToastProvider + useToast hook |
| src/components/EmptyState.tsx | 빈 상태 표시 |
| src/components/ConfirmDialog.tsx | 확인/취소 다이얼로그 |

### apps/admin/
| 파일 | 설명 |
|------|------|
| stores/auth-store.ts | Zustand — login, logout, hydrate, setTokens |
| hooks/useLoginLockout.ts | 점진적 지연 (6회:1분, 7회:5분, 8회:15분, 9회+:60분) |
| hooks/useAdminAuth.ts | 인증 hook (API 연동 deferred) |
| components/layout/AdminHeader.tsx | 햄버거+매장명+로그아웃 |
| components/layout/Sidebar.tsx | 4개 네비 (대시보드/테이블/메뉴/매장), 접힘 토글 |
| components/layout/AdminLayout.tsx | Header+Sidebar+Content, 모바일 반응형 |

### apps/customer/
| 파일 | 설명 |
|------|------|
| stores/auth-store.ts | Zustand — 테이블 인증 상태 관리 |
| hooks/useTableAuth.ts | 테이블 인증 hook (API 연동 deferred) |
| components/layout/CustomerHeader.tsx | 매장명+테이블뱃지+⚙️설정 |
| components/layout/BottomNav.tsx | 3탭 (메뉴/장바구니/주문내역) + 장바구니 뱃지 |
| components/layout/CustomerLayout.tsx | Header+Content+BottomNav |
| components/features/setup/SetupForm.tsx | 매장코드+테이블번호+비밀번호 폼 |
| components/features/setup/PasswordConfirmModal.tsx | 비밀번호 확인 모달 |

## Deferred Items
- `app/login/page.tsx` (admin) — react-hook-form + zod 폼, API 연동 시 완성
- `app/setup/page.tsx` (customer) — API 연동 시 완성
- 실제 API 호출 연동 — Backend Track 완료 후

## 테마 적용
- Customer: 흰 배경 + `#FF9900` 오렌지 accent + `#232F3E` 네이비 텍스트
- Admin: `#232F3E` 다크 사이드바 + `#F2F3F3` 라이트 콘텐츠
