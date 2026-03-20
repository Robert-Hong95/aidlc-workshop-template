# Test Plan - Unit 1-BE Auth API

## Unit Overview
- **Unit**: Unit 1-BE (Auth API)
- **Stories**: US-A01, US-C01
- **Requirements**: FR-A01, FR-C01, NFR-AUTH-01~07

---

## Domain Layer Tests

### Admin Entity
- **TC-AUTH-001**: isLocked() — 잠금 시간 이전이면 true 반환
  - Given: admin.lockedUntil = 미래 시각
  - When: admin.isLocked() 호출
  - Then: true 반환
  - Story: US-A01
  - Status: ⬜ Not Started

- **TC-AUTH-002**: isLocked() — 잠금 시간 경과 시 false 반환
  - Given: admin.lockedUntil = 과거 시각
  - When: admin.isLocked() 호출
  - Then: false 반환
  - Story: US-A01
  - Status: ⬜ Not Started

- **TC-AUTH-003**: incrementLoginAttempts() — 5회 미만이면 횟수만 증가
  - Given: admin.loginAttempts = 3
  - When: admin.incrementLoginAttempts() 호출
  - Then: loginAttempts = 4, lockedUntil = null
  - Story: US-A01
  - Status: ⬜ Not Started

- **TC-AUTH-004**: incrementLoginAttempts() — 5회 도달 시 15분 잠금
  - Given: admin.loginAttempts = 4
  - When: admin.incrementLoginAttempts() 호출
  - Then: loginAttempts = 5, lockedUntil = now + 15분
  - Story: US-A01
  - Status: ⬜ Not Started

- **TC-AUTH-005**: resetLoginAttempts() — 초기화
  - Given: admin.loginAttempts = 3, lockedUntil = 미래 시각
  - When: admin.resetLoginAttempts() 호출
  - Then: loginAttempts = 0, lockedUntil = null
  - Story: US-A01
  - Status: ⬜ Not Started

---

## Service Layer Tests

### AuthService.loginAdmin()
- **TC-AUTH-006**: 정상 로그인 성공
  - Given: 유효한 매장, 관리자, 비밀번호
  - When: loginAdmin(storeCode, username, password)
  - Then: TokenResponse 반환 (accessToken, role=ADMIN)
  - Story: US-A01
  - Status: ⬜ Not Started

- **TC-AUTH-007**: 매장 코드 없음 → STORE_NOT_FOUND
  - Given: 존재하지 않는 storeCode
  - When: loginAdmin(invalidCode, username, password)
  - Then: BusinessException(STORE_NOT_FOUND)
  - Story: US-A01
  - Status: ⬜ Not Started

- **TC-AUTH-008**: 사용자명 없음 → INVALID_CREDENTIALS
  - Given: 유효한 매장, 존재하지 않는 username
  - When: loginAdmin(storeCode, invalidUser, password)
  - Then: BusinessException(INVALID_CREDENTIALS)
  - Story: US-A01
  - Status: ⬜ Not Started

- **TC-AUTH-009**: 비밀번호 불일치 → INVALID_CREDENTIALS + 시도 횟수 증가
  - Given: 유효한 매장, 관리자, 잘못된 비밀번호
  - When: loginAdmin(storeCode, username, wrongPassword)
  - Then: BusinessException(INVALID_CREDENTIALS), loginAttempts 증가
  - Story: US-A01
  - Status: ⬜ Not Started

- **TC-AUTH-010**: 계정 잠금 상태 → LOGIN_ATTEMPTS_EXCEEDED
  - Given: admin.lockedUntil = 미래 시각
  - When: loginAdmin(storeCode, username, password)
  - Then: BusinessException(LOGIN_ATTEMPTS_EXCEEDED)
  - Story: US-A01
  - Status: ⬜ Not Started

- **TC-AUTH-011**: 5회 실패 시 계정 잠금
  - Given: admin.loginAttempts = 4
  - When: loginAdmin(storeCode, username, wrongPassword)
  - Then: BusinessException(INVALID_CREDENTIALS), admin.lockedUntil 설정됨
  - Story: US-A01
  - Status: ⬜ Not Started

- **TC-AUTH-012**: 로그인 성공 시 시도 횟수 리셋
  - Given: admin.loginAttempts = 3
  - When: loginAdmin(storeCode, username, correctPassword)
  - Then: TokenResponse 반환, loginAttempts = 0
  - Story: US-A01
  - Status: ⬜ Not Started

### AuthService.loginTable()
- **TC-AUTH-013**: 테이블 인증 성공
  - Given: 유효한 매장, 테이블, 비밀번호
  - When: loginTable(storeCode, tableNo, password)
  - Then: TokenResponse 반환 (role=TABLE)
  - Story: US-C01
  - Status: ⬜ Not Started

- **TC-AUTH-014**: 테이블 없음 → TABLE_NOT_FOUND
  - Given: 유효한 매장, 존재하지 않는 tableNo
  - When: loginTable(storeCode, invalidTableNo, password)
  - Then: BusinessException(TABLE_NOT_FOUND)
  - Story: US-C01
  - Status: ⬜ Not Started

- **TC-AUTH-015**: 테이블 비밀번호 불일치 → INVALID_CREDENTIALS
  - Given: 유효한 매장, 테이블, 잘못된 비밀번호
  - When: loginTable(storeCode, tableNo, wrongPassword)
  - Then: BusinessException(INVALID_CREDENTIALS)
  - Story: US-C01
  - Status: ⬜ Not Started

### AuthService.loginByQrToken()
- **TC-AUTH-016**: QR 토큰 인증 성공
  - Given: 유효한 QR JWT (type=QR, storeCode, tableNo)
  - When: loginByQrToken(qrToken)
  - Then: TokenResponse 반환 (role=TABLE)
  - Story: US-C01
  - Status: ⬜ Not Started

- **TC-AUTH-017**: 무효한 QR 토큰 → INVALID_CREDENTIALS
  - Given: 만료되거나 잘못된 QR JWT
  - When: loginByQrToken(invalidToken)
  - Then: BusinessException(INVALID_CREDENTIALS)
  - Story: US-C01
  - Status: ⬜ Not Started

- **TC-AUTH-018**: type=QR이 아닌 토큰 → INVALID_CREDENTIALS
  - Given: 일반 Access Token (type != QR)
  - When: loginByQrToken(accessToken)
  - Then: BusinessException(INVALID_CREDENTIALS)
  - Story: US-C01
  - Status: ⬜ Not Started

### AuthService.generateQrToken()
- **TC-AUTH-019**: QR 토큰 생성 성공
  - Given: 유효한 storeId, tableNo
  - When: generateQrToken(storeId, tableNo)
  - Then: QrTokenResponse 반환 (qrToken, qrUrl)
  - Story: US-C01
  - Status: ⬜ Not Started

### AuthService.refreshToken()
- **TC-AUTH-020**: Refresh Token 갱신 성공 (Rotation)
  - Given: 유효한 Refresh Token (tokenType=REFRESH)
  - When: refreshToken(refreshToken)
  - Then: 새 TokenResponse 반환 (새 accessToken)
  - Story: US-A01, US-C01
  - Status: ⬜ Not Started

- **TC-AUTH-021**: 무효한 Refresh Token → INVALID_CREDENTIALS
  - Given: 만료되거나 잘못된 Refresh Token
  - When: refreshToken(invalidToken)
  - Then: BusinessException(INVALID_CREDENTIALS)
  - Story: US-A01
  - Status: ⬜ Not Started

- **TC-AUTH-022**: Access Token으로 refresh 시도 → INVALID_CREDENTIALS
  - Given: tokenType=ACCESS인 토큰
  - When: refreshToken(accessToken)
  - Then: BusinessException(INVALID_CREDENTIALS)
  - Story: US-A01
  - Status: ⬜ Not Started

---

## Controller Layer Tests

### AdminAuthController
- **TC-AUTH-023**: POST /api/admin/auth/login — 성공 시 200 + Set-Cookie
  - Given: 유효한 LoginRequest
  - When: POST /api/admin/auth/login
  - Then: 200, body에 accessToken, Set-Cookie에 refreshToken
  - Story: US-A01
  - Status: ⬜ Not Started

- **TC-AUTH-024**: POST /api/admin/auth/login — 실패 시 401
  - Given: 잘못된 LoginRequest
  - When: POST /api/admin/auth/login
  - Then: 401, error message
  - Story: US-A01
  - Status: ⬜ Not Started

- **TC-AUTH-025**: POST /api/admin/auth/login — validation 실패 시 400
  - Given: 빈 필드가 있는 LoginRequest
  - When: POST /api/admin/auth/login
  - Then: 400, validation error
  - Story: US-A01
  - Status: ⬜ Not Started

### TableAuthController
- **TC-AUTH-026**: POST /api/customer/auth/login — 성공
  - Given: 유효한 TableLoginRequest
  - When: POST /api/customer/auth/login
  - Then: 200, accessToken + Set-Cookie
  - Story: US-C01
  - Status: ⬜ Not Started

- **TC-AUTH-027**: POST /api/customer/auth/qr — 성공
  - Given: 유효한 QrLoginRequest
  - When: POST /api/customer/auth/qr
  - Then: 200, accessToken + Set-Cookie
  - Story: US-C01
  - Status: ⬜ Not Started

### AuthCommonController
- **TC-AUTH-028**: POST /api/auth/refresh — 성공 (Cookie에서 refreshToken)
  - Given: 유효한 refreshToken Cookie
  - When: POST /api/auth/refresh
  - Then: 200, 새 accessToken + 새 Set-Cookie
  - Story: US-A01, US-C01
  - Status: ⬜ Not Started

- **TC-AUTH-029**: POST /api/auth/refresh — Cookie 없음 시 400
  - Given: refreshToken Cookie 없음
  - When: POST /api/auth/refresh
  - Then: 400
  - Story: US-A01
  - Status: ⬜ Not Started

---

## Modified Component Tests

### JwtTokenProvider
- **TC-AUTH-030**: createAccessToken — tokenType=ACCESS 클레임 포함
  - Given: subject, claims
  - When: createAccessToken()
  - Then: JWT에 tokenType=ACCESS 포함
  - Status: ⬜ Not Started

- **TC-AUTH-031**: createRefreshToken — tokenType=REFRESH 클레임 포함, 16시간 만료
  - Given: subject, claims
  - When: createRefreshToken()
  - Then: JWT에 tokenType=REFRESH 포함
  - Status: ⬜ Not Started

- **TC-AUTH-032**: createQrToken — type=QR 클레임 포함, 10분 만료
  - Given: subject, claims
  - When: createQrToken()
  - Then: JWT에 type=QR 포함
  - Status: ⬜ Not Started

---

## Requirements Coverage

| Requirement | Test Cases | Status |
|-------------|------------|--------|
| FR-A01 (매장 인증) | TC-AUTH-006~012, 023~025, 030~031 | ⬜ Pending |
| FR-C01 (테이블 자동 로그인) | TC-AUTH-013~019, 026~027, 032 | ⬜ Pending |
| NFR-AUTH-01 (성능) | TC-AUTH-006, 013 (응답시간 검증) | ⬜ Pending |
| NFR-AUTH-02 (토큰 전략) | TC-AUTH-020~022, 028~031 | ⬜ Pending |
| NFR-AUTH-04 (로그인 잠금) | TC-AUTH-001~005, 010~011 | ⬜ Pending |
