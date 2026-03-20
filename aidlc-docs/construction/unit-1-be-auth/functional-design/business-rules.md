# Business Rules - Unit 1-BE (Auth)

## 1. 인증 규칙

### BR-AUTH-01: 관리자 로그인 검증
- storeCode, username, password 모두 필수
- storeCode로 매장 존재 확인 → 없으면 INVALID_CREDENTIALS
- 매장 내 username 존재 확인 → 없으면 INVALID_CREDENTIALS
- bcrypt 비밀번호 매칭 → 불일치 시 INVALID_CREDENTIALS
- 보안: 매장/사용자/비밀번호 중 어느 것이 틀렸는지 구분하지 않음 (동일 에러 메시지)

### BR-AUTH-02: 테이블 인증 검증
- storeCode, tableNo, password 모두 필수
- storeCode로 매장 존재 확인 → 없으면 INVALID_CREDENTIALS
- 매장 내 tableNo 존재 확인 → 없으면 INVALID_CREDENTIALS
- bcrypt 비밀번호 매칭 → 불일치 시 INVALID_CREDENTIALS

### BR-AUTH-03: 로그인 시도 제한
- MVP에서는 미구현 (향후 추가 예정)

## 2. JWT 토큰 규칙

### BR-JWT-01: 관리자 토큰
- subject: admin.id (String)
- claims: { role: "ADMIN", storeId: Long, username: String }
- 만료: 16시간 (57,600,000ms) - application.yml의 jwt.expiration 사용

### BR-JWT-02: 테이블 토큰
- subject: table.id (String)
- claims: { role: "TABLE", storeId: Long, tableId: Long }
- 만료: 없음 - 별도 만료 시간 설정 필요 (기본 jwt.expiration 사용하지 않음)

### BR-JWT-03: 토큰 검증
- JwtAuthenticationFilter에서 자동 처리 (Foundation 구현 완료)
- role 클레임으로 ROLE_ADMIN / ROLE_TABLE 권한 부여

## 3. 관리자 등록 규칙

### BR-REG-01: 관리자 등록 검증
- storeCode로 매장 존재 확인 필수
- 동일 매장 내 username 중복 불가
- password: 최소 4자 이상 (MVP 기준 간단한 검증)
- username: 최소 2자 이상, 영문/숫자만 허용

### BR-REG-02: 비밀번호 저장
- BCryptPasswordEncoder로 해싱 후 저장
- 평문 비밀번호는 절대 저장하지 않음

## 4. DTO 정의

### Request DTOs
| DTO | 필드 | 검증 |
|-----|------|------|
| AdminLoginRequest | storeCode, username, password | 모두 @NotBlank |
| AdminRegisterRequest | storeCode, username, password | 모두 @NotBlank, password @Size(min=4), username @Pattern(영문숫자) |
| TableLoginRequest | storeCode, tableNo, password | storeCode/password @NotBlank, tableNo @NotNull @Min(1) |

### Response DTOs
| DTO | 필드 |
|-----|------|
| AdminLoginResponse | token, storeId, storeName, username |
| AdminRegisterResponse | adminId, storeId, username |
| TableLoginResponse | token, storeId, storeName, tableId, tableNo |
