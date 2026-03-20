# Test Plan - Unit 5-FE (공통 레이아웃 + Auth UI)

## Unit Overview
- **Unit**: Unit 5-FE
- **Stories**: US-A01 (매장 인증), US-C01 (테이블 자동 로그인)

---

## 1. Shared Packages Tests

### storage.ts
- **TC-5FE-001**: getItem returns parsed JSON for existing key
  - Given: localStorage에 `{"a":1}` 저장
  - When: `getItem('key')` 호출
  - Then: `{a:1}` 반환
  - Status: ⬜ Not Started

- **TC-5FE-002**: getItem returns null for missing key
  - Given: localStorage에 해당 키 없음
  - When: `getItem('missing')` 호출
  - Then: `null` 반환
  - Status: ⬜ Not Started

- **TC-5FE-003**: setItem stores JSON string
  - Given: 빈 localStorage
  - When: `setItem('key', {a:1})` 호출
  - Then: localStorage에 `'{"a":1}'` 저장됨
  - Status: ⬜ Not Started

- **TC-5FE-004**: removeItem deletes key
  - Given: localStorage에 키 존재
  - When: `removeItem('key')` 호출
  - Then: localStorage에서 삭제됨
  - Status: ⬜ Not Started

### format.ts
- **TC-5FE-005**: formatPrice formats number with comma and ₩
  - Given: `1000`
  - When: `formatPrice(1000)` 호출
  - Then: `"₩1,000"` 반환
  - Status: ⬜ Not Started

- **TC-5FE-006**: formatPrice handles zero
  - Given: `0`
  - When: `formatPrice(0)` 호출
  - Then: `"₩0"` 반환
  - Status: ⬜ Not Started

- **TC-5FE-007**: formatDateTime formats ISO string
  - Given: `"2026-03-20T13:00:00"`
  - When: `formatDateTime(...)` 호출
  - Then: `"2026-03-20 13:00"` 반환
  - Status: ⬜ Not Started

### validation.ts
- **TC-5FE-008**: isNotEmpty returns false for empty string
  - Given: `""`
  - When: `isNotEmpty("")` 호출
  - Then: `false` 반환
  - Status: ⬜ Not Started

- **TC-5FE-009**: isNotEmpty returns false for whitespace only
  - Given: `"   "`
  - When: `isNotEmpty("   ")` 호출
  - Then: `false` 반환
  - Status: ⬜ Not Started

- **TC-5FE-010**: isNotEmpty returns true for valid string
  - Given: `"hello"`
  - When: `isNotEmpty("hello")` 호출
  - Then: `true` 반환
  - Status: ⬜ Not Started

- **TC-5FE-011**: isPositiveNumber returns true for positive
  - Given: `5`
  - When: `isPositiveNumber(5)` 호출
  - Then: `true` 반환
  - Status: ⬜ Not Started

- **TC-5FE-012**: isPositiveNumber returns false for zero/negative
  - Given: `0`, `-1`
  - When: 호출
  - Then: `false` 반환
  - Status: ⬜ Not Started

### api-client client.ts
- **TC-5FE-013**: createApiClient adds Authorization header when token exists
  - Given: getToken이 "token123" 반환
  - When: GET 요청 전송
  - Then: `Authorization: Bearer token123` 헤더 포함
  - Status: ⬜ Not Started

- **TC-5FE-014**: createApiClient omits Authorization header when no token
  - Given: getToken이 null 반환
  - When: GET 요청 전송
  - Then: Authorization 헤더 없음
  - Status: ⬜ Not Started

- **TC-5FE-015**: createApiClient retries on 401 after token refresh
  - Given: 첫 요청 401, refresh 성공, 재시도 200
  - When: GET 요청 전송
  - Then: 최종 성공 응답 반환
  - Status: ⬜ Not Started

- **TC-5FE-016**: createApiClient calls onUnauthorized when refresh fails
  - Given: 401 응답, refresh도 실패
  - When: GET 요청 전송
  - Then: onUnauthorized 콜백 호출
  - Status: ⬜ Not Started

---

## 2. 관리자앱 Tests

### useLoginLockout hook
- **TC-5FE-017**: initial state has 0 attempts and not locked
  - Given: 초기 상태
  - When: hook 렌더링
  - Then: `attempts=0`, `isLocked=false`
  - Status: ⬜ Not Started

- **TC-5FE-018**: recordFailure increments attempts
  - Given: `attempts=0`
  - When: `recordFailure()` 호출
  - Then: `attempts=1`, `isLocked=false`
  - Status: ⬜ Not Started

- **TC-5FE-019**: 6th failure triggers 1 minute lockout
  - Given: `attempts=5`
  - When: `recordFailure()` 호출
  - Then: `attempts=6`, `isLocked=true`, `remainingSeconds=60`
  - Status: ⬜ Not Started

- **TC-5FE-020**: resetAttempts clears lockout
  - Given: `attempts=6`, locked
  - When: `resetAttempts()` 호출
  - Then: `attempts=0`, `isLocked=false`
  - Status: ⬜ Not Started

### auth-store (admin)
- **TC-5FE-021**: login sets auth state and persists to localStorage
  - Given: 초기 상태 (미인증)
  - When: `login(tokenResponse)` 호출
  - Then: `isAuthenticated=true`, localStorage에 토큰 저장
  - Status: ⬜ Not Started

- **TC-5FE-022**: logout clears auth state and localStorage
  - Given: 인증된 상태
  - When: `logout()` 호출
  - Then: `isAuthenticated=false`, localStorage 비워짐
  - Status: ⬜ Not Started

- **TC-5FE-023**: hydrate restores state from localStorage
  - Given: localStorage에 토큰 저장됨
  - When: `hydrate()` 호출
  - Then: store에 토큰 복원, `isAuthenticated=true`
  - Status: ⬜ Not Started

---

## 3. 고객앱 Tests

### auth-store (customer)
- **TC-5FE-024**: login sets table auth state and persists
  - Given: 초기 상태
  - When: `login(tableTokenResponse, storeCode)` 호출
  - Then: `isAuthenticated=true`, tableNo/storeCode 저장
  - Status: ⬜ Not Started

- **TC-5FE-025**: logout clears table auth state
  - Given: 인증된 상태
  - When: `logout()` 호출
  - Then: 모든 상태 초기화, localStorage 비워짐
  - Status: ⬜ Not Started

- **TC-5FE-026**: hydrate restores table auth from localStorage
  - Given: localStorage에 테이블 인증 정보 저장됨
  - When: `hydrate()` 호출
  - Then: store 복원
  - Status: ⬜ Not Started

---

## 4. UI Component Tests

### Button
- **TC-5FE-027**: renders with correct variant class
  - Given: `variant="primary"`
  - When: 렌더링
  - Then: primary 스타일 클래스 적용
  - Status: ⬜ Not Started

- **TC-5FE-028**: shows spinner when loading
  - Given: `loading=true`
  - When: 렌더링
  - Then: Spinner 표시, 클릭 비활성화
  - Status: ⬜ Not Started

### Input
- **TC-5FE-029**: displays error message when error prop set
  - Given: `error="필수 입력"`
  - When: 렌더링
  - Then: 에러 메시지 표시, 빨간 테두리
  - Status: ⬜ Not Started

### Modal
- **TC-5FE-030**: renders when isOpen=true, hidden when false
  - Given: `isOpen=true`
  - When: 렌더링
  - Then: 모달 표시
  - Status: ⬜ Not Started

- **TC-5FE-031**: calls onClose on ESC key
  - Given: 모달 열림
  - When: ESC 키 입력
  - Then: `onClose` 호출
  - Status: ⬜ Not Started

---

## Requirements Coverage
| Requirement | Test Cases | Status |
|-------------|------------|--------|
| US-A01 매장 인증 | TC-5FE-013~016, 017~023, 027~031 | ⬜ Pending |
| US-C01 테이블 자동 로그인 | TC-5FE-013~016, 024~026 | ⬜ Pending |
| 공유 유틸리티 | TC-5FE-001~012 | ⬜ Pending |
