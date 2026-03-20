# Business Rules - Unit 5-FE (공통 레이아웃 + Auth UI)

## 1. 관리자 인증 규칙

### BR-A01: 로그인 폼 검증
| 필드 | 규칙 |
|------|------|
| 매장코드 | 필수, 공백 불가 |
| 사용자명 | 필수, 공백 불가 |
| 비밀번호 | 필수, 공백 불가 |

### BR-A02: 점진적 로그인 시도 제한
- 실패 횟수 localStorage 키: `admin_login_attempts`
- 마지막 실패 시각 localStorage 키: `admin_login_last_fail`
- 대기 시간 계산: `getDelayMinutes(attempts)` → 6회:1분, 7회:5분, 8회:15분, 9회+:60분
- 로그인 성공 시 두 키 모두 삭제
- 대기 중: 제출 버튼 비활성화 + "N분 N초 후 재시도 가능" 카운트다운

### BR-A03: 관리자 토큰 관리
- Access Token: localStorage `admin_access_token`, 수명 1시간
- Refresh Token: localStorage `admin_refresh_token`, 수명 16시간
- 토큰 만료 판단: JWT payload의 `exp` 클레임 디코딩 (라이브러리 없이 base64 디코딩)
- 브라우저 새로고침 시 localStorage에서 복원

### BR-A04: 관리자 인증 라우트 가드
- 인증 필요 페이지: `/` (대시보드), `/tables`, `/menus`, `/stores`
- 미인증 시: `/login`으로 리다이렉트
- 인증 완료 후 `/login` 접근 시: `/`로 리다이렉트

---

## 2. 고객 인증 규칙

### BR-C01: 초기 설정 폼 검증
| 필드 | 규칙 |
|------|------|
| 매장코드 | 필수, 공백 불가 |
| 테이블번호 | 필수, 숫자만, 1 이상 |
| 비밀번호 | 필수, 공백 불가 |

### BR-C02: 고객 토큰 관리
- Access Token: localStorage `table_access_token`, 수명 1시간
- Refresh Token: localStorage `table_refresh_token`, 수명 16시간
- 매장코드: localStorage `table_store_code`
- 테이블번호: localStorage `table_no`
- 매장명: localStorage `store_name`

### BR-C03: 고객 인증 라우트 가드
- 인증 필요 페이지: `/` (메뉴), `/cart`, `/order/confirm`, `/orders`
- 미인증 시: `/setup`으로 리다이렉트
- 인증 완료 후 `/setup` 접근 시: `/`로 리다이렉트 (설정 재진입은 비밀번호 확인 후만 가능)

### BR-C04: 설정 재진입 비밀번호 확인
- Header 설정(⚙️) 아이콘 클릭 → 비밀번호 확인 모달
- 서버 검증 API 호출 (POST /api/customer/auth/verify-password)
- 성공 시: 기존 토큰 삭제 → `/setup`으로 이동
- 실패 시: "비밀번호가 올바르지 않습니다" 에러 표시

---

## 3. API Client 규칙

### BR-API01: 토큰 갱신
- 갱신 엔드포인트: `POST /api/{admin|customer}/auth/refresh`
- 요청 body: `{ refreshToken: string }`
- 갱신 중 동시 요청 큐잉 (중복 갱신 방지)

### BR-API02: 에러 응답 처리
| HTTP 상태 | 처리 |
|-----------|------|
| 401 | 토큰 갱신 시도 → 실패 시 로그인/설정 페이지 이동 |
| 403 | "접근 권한이 없습니다" 토스트 |
| 400 | 서버 에러 메시지 표시 |
| 500 | "서버 오류가 발생했습니다" 토스트 |
| 네트워크 에러 | "네트워크 연결을 확인해주세요" 토스트 |
