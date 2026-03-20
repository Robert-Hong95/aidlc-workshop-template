# Unit 1-BE Auth API - Domain Entities

## Admin (관리자)

### 테이블: admins
| 필드 | 타입 | 제약조건 | 설명 |
|------|------|---------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 관리자 ID |
| store_id | BIGINT | FK(stores.id), NOT NULL | 소속 매장 |
| username | VARCHAR(50) | NOT NULL | 사용자명 |
| password | VARCHAR(255) | NOT NULL | bcrypt 해싱 비밀번호 |
| login_attempts | INT | NOT NULL, DEFAULT 0 | 연속 로그인 실패 횟수 |
| locked_until | DATETIME | NULL | 잠금 해제 시각 (NULL=잠금 없음) |
| created_at | DATETIME | NOT NULL, DEFAULT NOW() | 생성일시 |
| | | UNIQUE(store_id, username) | 매장 내 유일 |

### JPA Entity 설계
```java
@Entity
@Table(name = "admins", uniqueConstraints = @UniqueConstraint(columns = {"store_id", "username"}))
public class Admin {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "store_id", nullable = false)
    private Long storeId;
    
    @Column(nullable = false, length = 50)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(name = "login_attempts", nullable = false)
    private int loginAttempts = 0;
    
    @Column(name = "locked_until")
    private LocalDateTime lockedUntil;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // 비즈니스 메서드
    public boolean isLocked() { ... }
    public void incrementLoginAttempts() { ... }
    public void resetLoginAttempts() { ... }
    public void lock(Duration duration) { ... }
}
```

---

## Store (매장) — 참조만 (Unit 2에서 상세 구현)

Unit 1에서는 Store 엔티티를 **읽기 전용으로 참조**합니다.
- `StoreRepository.findByStoreCode(code)` — 로그인 시 매장 확인용
- Store 엔티티 자체는 최소한으로 정의 (id, storeCode, name)

### JPA Entity (최소 정의)
```java
@Entity
@Table(name = "stores")
public class Store {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "store_code", nullable = false, unique = true, length = 50)
    private String storeCode;
    
    @Column(nullable = false, length = 100)
    private String name;
}
```

---

## StoreTable (테이블) — 참조만 (Unit 2에서 상세 구현)

Unit 1에서는 StoreTable 엔티티를 **읽기 전용으로 참조**합니다.
- `StoreTableRepository.findByStoreIdAndTableNo(storeId, tableNo)` — 테이블 인증용

### JPA Entity (최소 정의)
```java
@Entity
@Table(name = "store_tables", uniqueConstraints = @UniqueConstraint(columns = {"store_id", "table_no"}))
public class StoreTable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "store_id", nullable = false)
    private Long storeId;
    
    @Column(name = "table_no", nullable = false)
    private int tableNo;
    
    @Column(nullable = false)
    private String password;
}
```
