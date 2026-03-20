# Test Plan - Unit 1-BE (Auth API)

## Unit Overview
- **Unit**: Unit 1-BE Auth API
- **Stories**: US-A01 (매장 인증), US-C01 (테이블 자동 로그인)

---

## Service Layer Tests

### AuthService.loginAdmin()

- **TC-AUTH-001**: 유효한 정보로 관리자 로그인 성공
  - Given: 매장(storeCode="STORE01")과 관리자(username="admin", password="pass1234")가 존재
  - When: loginAdmin("STORE01", "admin", "pass1234") 호출
  - Then: JWT 토큰, storeId, storeName, username이 포함된 응답 반환
  - Story: US-A01
  - Status: ⬜ Not Started

- **TC-AUTH-002**: 존재하지 않는 매장 코드로 로그인 실패
  - Given: "INVALID" 매장 코드가 존재하지 않음
  - When: loginAdmin("INVALID", "admin", "pass1234") 호출
  - Then: INVALID_CREDENTIALS 예외 발생
  - Story: US-A01
  - Status: ⬜ Not Started

- **TC-AUTH-003**: 존재하지 않는 username으로 로그인 실패
  - Given: 매장은 존재하지만 "unknown" 사용자가 없음
  - When: loginAdmin("STORE01", "unknown", "pass1234") 호출
  - Then: INVALID_CREDENTIALS 예외 발생
  - Story: US-A01
  - Status: ⬜ Not Started

- **TC-AUTH-004**: 잘못된 비밀번호로 로그인 실패
  - Given: 매장과 관리자가 존재하지만 비밀번호가 다름
  - When: loginAdmin("STORE01", "admin", "wrongpass") 호출
  - Then: INVALID_CREDENTIALS 예외 발생
  - Story: US-A01
  - Status: ⬜ Not Started

### AuthService.registerAdmin()

- **TC-AUTH-005**: 유효한 정보로 관리자 등록 성공
  - Given: 매장(storeCode="STORE01")이 존재하고 "newadmin" 사용자가 없음
  - When: registerAdmin("STORE01", "newadmin", "pass1234") 호출
  - Then: adminId, storeId, username이 포함된 응답 반환, 비밀번호 bcrypt 해싱 저장
  - Story: US-A01
  - Status: ⬜ Not Started

- **TC-AUTH-006**: 존재하지 않는 매장에 관리자 등록 실패
  - Given: "INVALID" 매장이 존재하지 않음
  - When: registerAdmin("INVALID", "admin", "pass1234") 호출
  - Then: STORE_NOT_FOUND 예외 발생
  - Story: US-A01
  - Status: ⬜ Not Started

- **TC-AUTH-007**: 동일 매장 내 중복 username 등록 실패
  - Given: 매장에 "admin" 사용자가 이미 존재
  - When: registerAdmin("STORE01", "admin", "pass1234") 호출
  - Then: DUPLICATE_ADMIN 예외 발생
  - Story: US-A01
  - Status: ⬜ Not Started

### AuthService.loginTable()

- **TC-AUTH-008**: 유효한 정보로 테이블 인증 성공
  - Given: 매장(storeCode="STORE01")과 테이블(tableNo=1, password="table123")이 존재
  - When: loginTable("STORE01", 1, "table123") 호출
  - Then: JWT 토큰, storeId, storeName, tableId, tableNo가 포함된 응답 반환
  - Story: US-C01
  - Status: ⬜ Not Started

- **TC-AUTH-009**: 존재하지 않는 매장으로 테이블 인증 실패
  - Given: "INVALID" 매장이 존재하지 않음
  - When: loginTable("INVALID", 1, "table123") 호출
  - Then: INVALID_CREDENTIALS 예외 발생
  - Story: US-C01
  - Status: ⬜ Not Started

- **TC-AUTH-010**: 존재하지 않는 테이블 번호로 인증 실패
  - Given: 매장은 존재하지만 테이블 번호 99가 없음
  - When: loginTable("STORE01", 99, "table123") 호출
  - Then: INVALID_CREDENTIALS 예외 발생
  - Story: US-C01
  - Status: ⬜ Not Started

- **TC-AUTH-011**: 잘못된 비밀번호로 테이블 인증 실패
  - Given: 매장과 테이블이 존재하지만 비밀번호가 다름
  - When: loginTable("STORE01", 1, "wrongpass") 호출
  - Then: INVALID_CREDENTIALS 예외 발생
  - Story: US-C01
  - Status: ⬜ Not Started

---

## Controller Layer Tests

### AdminAuthController

- **TC-AUTH-012**: POST /api/admin/auth/login 성공 (200)
  - Given: 유효한 AdminLoginRequest
  - When: POST /api/admin/auth/login
  - Then: 200 OK, ApiResponse(success=true, data=AdminLoginResponse)
  - Story: US-A01
  - Status: ⬜ Not Started

- **TC-AUTH-013**: POST /api/admin/auth/login 실패 - 잘못된 인증 (401)
  - Given: 잘못된 인증 정보
  - When: POST /api/admin/auth/login
  - Then: 401 Unauthorized
  - Story: US-A01
  - Status: ⬜ Not Started

- **TC-AUTH-014**: POST /api/admin/auth/register 성공 (200)
  - Given: 유효한 AdminRegisterRequest
  - When: POST /api/admin/auth/register
  - Then: 200 OK, ApiResponse(success=true, data=AdminRegisterResponse)
  - Story: US-A01
  - Status: ⬜ Not Started

- **TC-AUTH-015**: POST /api/admin/auth/register 실패 - validation (400)
  - Given: 빈 필드가 있는 AdminRegisterRequest
  - When: POST /api/admin/auth/register
  - Then: 400 Bad Request
  - Story: US-A01
  - Status: ⬜ Not Started

### TableAuthController

- **TC-AUTH-016**: POST /api/customer/auth/login 성공 (200)
  - Given: 유효한 TableLoginRequest
  - When: POST /api/customer/auth/login
  - Then: 200 OK, ApiResponse(success=true, data=TableLoginResponse)
  - Story: US-C01
  - Status: ⬜ Not Started

- **TC-AUTH-017**: POST /api/customer/auth/login 실패 - 잘못된 인증 (401)
  - Given: 잘못된 인증 정보
  - When: POST /api/customer/auth/login
  - Then: 401 Unauthorized
  - Story: US-C01
  - Status: ⬜ Not Started

---

## Requirements Coverage

| Story | Test Cases | Status |
|-------|-----------|--------|
| US-A01 (매장 인증) | TC-AUTH-001~007, TC-AUTH-012~015 | 🟢 Passed |
| US-C01 (테이블 자동 로그인) | TC-AUTH-008~011, TC-AUTH-016~017 | 🟢 Passed |
