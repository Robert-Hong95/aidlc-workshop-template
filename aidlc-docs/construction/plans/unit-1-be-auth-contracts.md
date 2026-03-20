# Contract/Interface Definition - Unit 1-BE (Auth API)

## Unit Context
- **Stories**: US-A01 (매장 인증), US-C01 (테이블 자동 로그인)
- **Dependencies**: Unit 0 Foundation (JwtTokenProvider, SecurityConfig, PasswordEncoder)
- **Database Entities**: Admin (owned), Store (referenced), StoreTable (referenced)

---

## Domain Layer

### Admin
```java
@Entity @Table(name = "admins")
public class Admin {
    Long id;
    Long storeId;
    String username;
    String password;
    LocalDateTime createdAt;
}
```

### Store (Auth에서 참조용 - Unit 2에서 본격 구현)
```java
@Entity @Table(name = "stores")
public class Store {
    Long id;
    String storeCode;
    String name;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
```

### StoreTable (Auth에서 참조용 - Unit 2에서 본격 구현)
```java
@Entity @Table(name = "store_tables")
public class StoreTable {
    Long id;
    Long storeId;
    Integer tableNo;
    String password;
    LocalDateTime createdAt;
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
    Optional<StoreTable> findByStoreIdAndTableNo(Long storeId, Integer tableNo);
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
     * @param request AdminLoginRequest (storeCode, username, password)
     * @return AdminLoginResponse (token, storeId, storeName, username)
     * @throws BusinessException INVALID_CREDENTIALS - 매장/사용자/비밀번호 불일치
     */
    AdminLoginResponse loginAdmin(AdminLoginRequest request);

    /**
     * 관리자 등록
     * @param request AdminRegisterRequest (storeCode, username, password)
     * @return AdminRegisterResponse (adminId, storeId, username)
     * @throws BusinessException STORE_NOT_FOUND - 매장 없음
     * @throws BusinessException DUPLICATE_ADMIN - 동일 매장 내 username 중복
     */
    AdminRegisterResponse registerAdmin(AdminRegisterRequest request);

    /**
     * 테이블 인증
     * @param request TableLoginRequest (storeCode, tableNo, password)
     * @return TableLoginResponse (token, storeId, storeName, tableId, tableNo)
     * @throws BusinessException INVALID_CREDENTIALS - 매장/테이블/비밀번호 불일치
     */
    TableLoginResponse loginTable(TableLoginRequest request);
}
```

---

## Controller Layer (API)

### AdminAuthController
```java
@RestController @RequestMapping("/api/admin/auth")
public class AdminAuthController {
    /**
     * POST /api/admin/auth/login
     * @param request AdminLoginRequest
     * @return ApiResponse<AdminLoginResponse>
     */
    ApiResponse<AdminLoginResponse> login(AdminLoginRequest request);

    /**
     * POST /api/admin/auth/register
     * @param request AdminRegisterRequest
     * @return ApiResponse<AdminRegisterResponse>
     */
    ApiResponse<AdminRegisterResponse> register(AdminRegisterRequest request);
}
```

### TableAuthController
```java
@RestController @RequestMapping("/api/customer/auth")
public class TableAuthController {
    /**
     * POST /api/customer/auth/login
     * @param request TableLoginRequest
     * @return ApiResponse<TableLoginResponse>
     */
    ApiResponse<TableLoginResponse> login(TableLoginRequest request);
}
```

---

## DTO Layer

### Request DTOs
```java
public record AdminLoginRequest(
    @NotBlank String storeCode,
    @NotBlank String username,
    @NotBlank String password
) {}

public record AdminRegisterRequest(
    @NotBlank String storeCode,
    @NotBlank @Size(min = 2) @Pattern(regexp = "^[a-zA-Z0-9]+$") String username,
    @NotBlank @Size(min = 4) String password
) {}

public record TableLoginRequest(
    @NotBlank String storeCode,
    @NotNull @Min(1) Integer tableNo,
    @NotBlank String password
) {}
```

### Response DTOs
```java
public record AdminLoginResponse(String token, Long storeId, String storeName, String username) {}
public record AdminRegisterResponse(Long adminId, Long storeId, String username) {}
public record TableLoginResponse(String token, Long storeId, String storeName, Long tableId, Integer tableNo) {}
```
