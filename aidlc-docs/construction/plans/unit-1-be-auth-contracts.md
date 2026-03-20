# Contract/Interface Definition - Unit 1-BE Auth API

## Unit Context
- **Stories**: US-A01 (매장 인증), US-C01 (테이블 자동 로그인)
- **Dependencies**: Unit 0 (Foundation — SecurityConfig, JwtTokenProvider, JwtAuthenticationFilter, ErrorCode)
- **Database Entities**: Admin (owned), Store (read-only ref), StoreTable (read-only ref)

---

## Domain Layer

### Admin
```java
@Entity @Table(name = "admins")
public class Admin {
    Long getId();
    Long getStoreId();
    String getUsername();
    String getPassword();
    int getLoginAttempts();
    LocalDateTime getLockedUntil();
    LocalDateTime getCreatedAt();

    boolean isLocked();                    // lockedUntil != null && lockedUntil.isAfter(now)
    void incrementLoginAttempts();         // loginAttempts++; if >= 5 → lock(15min)
    void resetLoginAttempts();             // loginAttempts=0, lockedUntil=null
    void lock(Duration duration);          // lockedUntil = now + duration
}
```

### Store (read-only ref)
```java
@Entity @Table(name = "stores")
public class Store {
    Long getId();
    String getStoreCode();
    String getName();
}
```

### StoreTable (read-only ref)
```java
@Entity @Table(name = "store_tables")
public class StoreTable {
    Long getId();
    Long getStoreId();
    int getTableNo();
    String getPassword();
}
```

---

## Repository Layer

### AdminRepository
```java
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByStoreIdAndUsername(Long storeId, String username);
}
```

### StoreRepository
```java
public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByStoreCode(String storeCode);
}
```

### StoreTableRepository
```java
public interface StoreTableRepository extends JpaRepository<StoreTable, Long> {
    Optional<StoreTable> findByStoreIdAndTableNo(Long storeId, int tableNo);
}
```

---

## Service Layer

### AuthService
```java
@Service
public class AuthService {
    /**
     * 관리자 로그인
     * @param storeCode 매장 코드
     * @param username 사용자명
     * @param password 비밀번호
     * @return TokenResponse (accessToken, refreshToken via cookie)
     * @throws BusinessException STORE_NOT_FOUND, INVALID_CREDENTIALS, LOGIN_ATTEMPTS_EXCEEDED
     */
    TokenResponse loginAdmin(String storeCode, String username, String password);

    /**
     * 테이블 인증 (태블릿)
     * @param storeCode 매장 코드
     * @param tableNo 테이블 번호
     * @param password 비밀번호
     * @return TokenResponse
     * @throws BusinessException STORE_NOT_FOUND, TABLE_NOT_FOUND, INVALID_CREDENTIALS
     */
    TokenResponse loginTable(String storeCode, int tableNo, String password);

    /**
     * QR 코드 인증
     * @param qrToken QR JWT 토큰
     * @return TokenResponse
     * @throws BusinessException INVALID_CREDENTIALS
     */
    TokenResponse loginByQrToken(String qrToken);

    /**
     * QR 토큰 생성 (관리자용)
     * @param storeId 매장 ID
     * @param tableNo 테이블 번호
     * @return QrTokenResponse
     * @throws BusinessException STORE_NOT_FOUND, TABLE_NOT_FOUND
     */
    QrTokenResponse generateQrToken(Long storeId, int tableNo);

    /**
     * Refresh Token으로 Access Token 갱신
     * @param refreshToken Refresh JWT
     * @return TokenResponse (새 accessToken + 새 refreshToken)
     * @throws BusinessException INVALID_CREDENTIALS
     */
    TokenResponse refreshToken(String refreshToken);
}
```

---

## Controller Layer

### AdminAuthController
```java
@RestController @RequestMapping("/api/admin/auth")
public class AdminAuthController {
    /**
     * POST /api/admin/auth/login
     * Body: LoginRequest {storeCode, username, password}
     * Response: ApiResponse<TokenResponse> + Set-Cookie: refreshToken
     */
    ResponseEntity<ApiResponse<TokenResponse>> login(LoginRequest request);

    /**
     * POST /api/admin/auth/logout
     * Response: ApiResponse<Void> + Clear refreshToken cookie
     */
    ResponseEntity<ApiResponse<Void>> logout();
}
```

### TableAuthController
```java
@RestController @RequestMapping("/api/customer/auth")
public class TableAuthController {
    /**
     * POST /api/customer/auth/login
     * Body: TableLoginRequest {storeCode, tableNo, password}
     * Response: ApiResponse<TokenResponse> + Set-Cookie: refreshToken
     */
    ResponseEntity<ApiResponse<TokenResponse>> login(TableLoginRequest request);

    /**
     * POST /api/customer/auth/qr
     * Body: QrLoginRequest {qrToken}
     * Response: ApiResponse<TokenResponse> + Set-Cookie: refreshToken
     */
    ResponseEntity<ApiResponse<TokenResponse>> loginByQr(QrLoginRequest request);
}
```

### AuthCommonController
```java
@RestController @RequestMapping("/api/auth")
public class AuthCommonController {
    /**
     * POST /api/auth/refresh
     * Cookie: refreshToken
     * Response: ApiResponse<TokenResponse> + Set-Cookie: refreshToken (rotation)
     */
    ResponseEntity<ApiResponse<TokenResponse>> refresh(@CookieValue String refreshToken);
}
```

### QrTokenController (관리자 전용)
```java
@RestController @RequestMapping("/api/admin/tables")
public class QrTokenController {
    /**
     * POST /api/admin/tables/qr-token
     * Body: QrTokenRequest {storeId, tableNo}
     * Response: ApiResponse<QrTokenResponse>
     * Auth: ADMIN required
     */
    ResponseEntity<ApiResponse<QrTokenResponse>> generateQrToken(QrTokenRequest request);
}
```

---

## DTO Layer

### Request
```java
public record LoginRequest(@NotBlank String storeCode, @NotBlank String username, @NotBlank String password) {}
public record TableLoginRequest(@NotBlank String storeCode, @NotNull Integer tableNo, @NotBlank String password) {}
public record QrLoginRequest(@NotBlank String qrToken) {}
public record QrTokenRequest(@NotNull Long storeId, @NotNull Integer tableNo) {}
```

### Response
```java
public record TokenResponse(String accessToken, long expiresIn, String role) {}
public record QrTokenResponse(String qrToken, String qrUrl, long expiresIn) {}
```

---

## Modified Components (Unit 0)

### JwtTokenProvider (수정)
```java
String createAccessToken(String subject, Map<String, Object> claims);   // 1시간
String createRefreshToken(String subject, Map<String, Object> claims);  // 16시간
String createQrToken(String subject, Map<String, Object> claims);       // 10분
Claims parseToken(String token);
boolean validateToken(String token);
```

### JwtAuthenticationFilter (수정)
- tokenType=ACCESS인 경우만 인증 처리

### SecurityConfig (수정)
- `/api/auth/refresh`, `/api/admin/auth/logout` permitAll 추가
