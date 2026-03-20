# Unit 1-BE Auth API - NFR Design Patterns

## 1. Token 패턴: Access + Refresh with Rotation

### 구조
```
[로그인 성공]
  → Access Token (1시간) → Response Body (JSON)
  → Refresh Token (16시간) → HttpOnly Cookie

[Access Token 만료]
  → POST /api/auth/refresh (Cookie에서 Refresh Token 자동 전송)
  → 새 Access Token → Response Body
  → 새 Refresh Token → HttpOnly Cookie (Rotation)

[Refresh Token 만료]
  → 재로그인 필요
```

### HttpOnly Cookie 설정
```
Set-Cookie: refreshToken={jwt}; HttpOnly; Secure; SameSite=Strict; Path=/api/auth/refresh; Max-Age=57600
```
- `HttpOnly`: JavaScript 접근 차단 (XSS 방어)
- `Secure`: 개발 환경에서는 false 허용 (localhost)
- `SameSite=Strict`: CSRF 방어
- `Path=/api/auth/refresh`: refresh 엔드포인트에서만 전송
- `Max-Age=57600`: 16시간 (초 단위)

### JwtTokenProvider 변경사항
- `createAccessToken(subject, claims)` — 만료 1시간, tokenType=ACCESS
- `createRefreshToken(subject, claims)` — 만료 16시간, tokenType=REFRESH
- `parseToken()` — tokenType 검증 추가

### JwtAuthenticationFilter 변경사항
- Authorization 헤더의 Access Token만 검증
- tokenType=ACCESS인 경우만 인증 처리
- Refresh Token은 `/api/auth/refresh` 엔드포인트에서만 Cookie로 처리

## 2. 로그인 잠금 패턴: DB-based Account Locking

```
[로그인 실패]
  → admin.loginAttempts += 1
  → if loginAttempts >= 5:
      admin.lockedUntil = now + 15분
  → save to DB

[로그인 시도]
  → if admin.lockedUntil != null && admin.lockedUntil > now:
      throw LOGIN_ATTEMPTS_EXCEEDED
  → if admin.lockedUntil != null && admin.lockedUntil <= now:
      자동 해제 (loginAttempts=0, lockedUntil=null)

[로그인 성공]
  → admin.loginAttempts = 0
  → admin.lockedUntil = null
```

## 3. 보안 로깅 패턴: Failure-only Audit Log

```java
// AuthService 내부
private static final Logger log = LoggerFactory.getLogger(AuthService.class);

// 로그인 실패 시
log.warn("Login failed: username={}, storeCode={}, reason={}", username, storeCode, reason);
```
- WARN 레벨로 실패만 기록
- 비밀번호는 절대 로깅하지 않음

## 4. CORS 패턴: Whitelist-based

```
허용 Origins: http://localhost:3000, http://localhost:3001
허용 Methods: GET, POST, PUT, DELETE, OPTIONS
허용 Headers: Authorization, Content-Type
Credentials: true (Cookie 전송 허용 — Refresh Token용)
```
- `credentials: true`가 Refresh Token HttpOnly Cookie 전송에 필수

## 5. API 엔드포인트 최종 정리

| Method | Path | Auth | 설명 |
|--------|------|------|------|
| POST | `/api/admin/auth/login` | 불필요 | 관리자 로그인 → Access(body) + Refresh(cookie) |
| POST | `/api/customer/auth/login` | 불필요 | 테이블 인증 → Access(body) + Refresh(cookie) |
| POST | `/api/customer/auth/qr` | 불필요 | QR 인증 → Access(body) + Refresh(cookie) |
| POST | `/api/auth/refresh` | Cookie | 토큰 갱신 → 새 Access(body) + 새 Refresh(cookie) |
| POST | `/api/admin/auth/logout` | ADMIN | 로그아웃 → Refresh Cookie 삭제 |
| POST | `/api/admin/tables/qr-token` | ADMIN | QR 토큰 생성 |
