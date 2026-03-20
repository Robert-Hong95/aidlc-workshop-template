# Unit 1-BE Auth API - Business Logic Model

## 1. 관리자 로그인 플로우

```
AdminAuthController.login(LoginRequest)
  → AuthService.loginAdmin(storeCode, username, password)
    1. StoreRepository.findByStoreCode(storeCode)
       → 없으면: STORE_NOT_FOUND 예외
    2. AdminRepository.findByStoreIdAndUsername(storeId, username)
       → 없으면: INVALID_CREDENTIALS 예외
    3. Admin.isLocked() 확인
       → 잠금 상태: LOGIN_ATTEMPTS_EXCEEDED 예외
    4. PasswordEncoder.matches(password, admin.password)
       → 불일치:
         a. admin.incrementLoginAttempts()
         b. 5회 도달 시 admin.lock(15분)
         c. AdminRepository.save(admin)
         d. INVALID_CREDENTIALS 예외
    5. admin.resetLoginAttempts()
    6. AdminRepository.save(admin)
    7. JwtTokenProvider.createToken(subject=adminId, claims={storeId, adminId, username, storeName, role=ADMIN})
    8. return TokenResponse(token, expiresIn)
```

## 2. 테이블 인증 플로우 (태블릿)

```
TableAuthController.login(TableLoginRequest)
  → AuthService.loginTable(storeCode, tableNo, password)
    1. StoreRepository.findByStoreCode(storeCode)
       → 없으면: STORE_NOT_FOUND 예외
    2. StoreTableRepository.findByStoreIdAndTableNo(storeId, tableNo)
       → 없으면: TABLE_NOT_FOUND 예외
    3. PasswordEncoder.matches(password, storeTable.password)
       → 불일치: INVALID_CREDENTIALS 예외
    4. JwtTokenProvider.createToken(subject=tableId, claims={storeId, tableId, tableNo, storeName, role=TABLE})
    5. return TokenResponse(token, expiresIn)
```

## 3. QR 코드 인증 플로우

```
TableAuthController.loginByQr(qrToken)
  → AuthService.loginByQrToken(qrToken)
    1. JwtTokenProvider.validateToken(qrToken)
       → 무효: INVALID_CREDENTIALS 예외
    2. Claims에서 storeCode, tableNo 추출
    3. claims.get("type") == "QR" 확인
       → 아니면: INVALID_CREDENTIALS 예외
    4. StoreRepository.findByStoreCode(storeCode)
    5. StoreTableRepository.findByStoreIdAndTableNo(storeId, tableNo)
    6. 일반 테이블 JWT 발급 (role=TABLE)
    7. return TokenResponse(token, expiresIn)
```

## 4. QR 토큰 생성 플로우 (관리자용)

```
TableAuthController.generateQrToken(storeId, tableNo)  [ADMIN 권한 필요]
  → AuthService.generateQrToken(storeId, tableNo)
    1. StoreRepository.findById(storeId) → 매장 확인
    2. StoreTableRepository.findByStoreIdAndTableNo(storeId, tableNo) → 테이블 확인
    3. JwtTokenProvider.createToken(subject="qr", claims={storeCode, tableNo, type=QR}, expiration=짧은시간)
    4. return QrTokenResponse(qrToken, qrUrl)
```

## 5. 토큰 검증 플로우 (기존 JwtAuthenticationFilter에서 처리)

기존 Unit 0의 `JwtAuthenticationFilter`가 모든 요청에서 JWT를 검증합니다.
- Authorization 헤더에서 Bearer 토큰 추출
- Claims에서 role 추출 → Spring Security Authority 설정
- Claims를 Authentication.details에 저장 → Controller에서 접근 가능


---

## 6. DTO 설계

### Request DTOs

**LoginRequest** (관리자 로그인)
```java
public record LoginRequest(
    @NotBlank String storeCode,
    @NotBlank String username,
    @NotBlank String password
) {}
```

**TableLoginRequest** (테이블 인증)
```java
public record TableLoginRequest(
    @NotBlank String storeCode,
    @NotNull Integer tableNo,
    @NotBlank String password
) {}
```

**QrLoginRequest** (QR 인증)
```java
public record QrLoginRequest(
    @NotBlank String qrToken
) {}
```

**QrTokenRequest** (QR 토큰 생성 - 관리자용)
```java
public record QrTokenRequest(
    @NotNull Long storeId,
    @NotNull Integer tableNo
) {}
```

### Response DTOs

**TokenResponse**
```java
public record TokenResponse(
    String token,
    long expiresIn,
    String role
) {}
```

**QrTokenResponse**
```java
public record QrTokenResponse(
    String qrToken,
    String qrUrl,
    long expiresIn
) {}
```

---

## 7. API 엔드포인트 설계

### AdminAuthController

| Method | Path | Auth | Request | Response | 설명 |
|--------|------|------|---------|----------|------|
| POST | `/api/admin/auth/login` | 불필요 | LoginRequest | TokenResponse | 관리자 로그인 |

### TableAuthController

| Method | Path | Auth | Request | Response | 설명 |
|--------|------|------|---------|----------|------|
| POST | `/api/customer/auth/login` | 불필요 | TableLoginRequest | TokenResponse | 테이블 인증 |
| POST | `/api/customer/auth/qr` | 불필요 | QrLoginRequest | TokenResponse | QR 코드 인증 |
| POST | `/api/admin/tables/qr-token` | ADMIN | QrTokenRequest | QrTokenResponse | QR 토큰 생성 |
