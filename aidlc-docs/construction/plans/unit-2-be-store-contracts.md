# Contract/Interface Definition - Unit 2-BE (Store/Table API)

## Unit Context
- **Stories**: US-A05 (매장 관리), US-A03 (테이블 관리)
- **Dependencies**: Unit 0 (Foundation), Unit 1 (Auth - Store/StoreTable entities, repositories)
- **Database Entities**: TableSession (owned), Store (modify), StoreTable (modify)

---

## Domain Layer

### TableSession (신규)
```java
@Entity @Table(name = "table_sessions")
public class TableSession {
    Long id;
    Long tableId;
    LocalDateTime startedAt;
    LocalDateTime endedAt;
    
    void end();       // endedAt = now()
    boolean isActive(); // endedAt == null
}
```

### Store (수정 - updateName 추가)
```java
void updateName(String name); // name 변경, updatedAt 갱신
```

## Repository Layer

### TableSessionRepository
```java
public interface TableSessionRepository extends JpaRepository<TableSession, Long> {
    Optional<TableSession> findByTableIdAndEndedAtIsNull(Long tableId);
}
```

### StoreTableRepository (추가 메서드)
```java
List<StoreTable> findAllByStoreId(Long storeId);
```

## Service Layer

### StoreService
```java
StoreResponse createStore(StoreCreateRequest request);
List<StoreResponse> getStores();
StoreResponse getStore(Long storeId);
StoreResponse updateStore(Long storeId, StoreUpdateRequest request);
void deleteStore(Long storeId);
```

### TableService
```java
TableResponse setupTable(Long storeId, TableSetupRequest request);
List<TableResponse> getTables(Long storeId);
void endSession(Long tableId);
List<OrderHistoryResponse> getOrderHistory(Long tableId, LocalDate dateFrom, LocalDate dateTo);
```

## Controller Layer

### AdminStoreController - /api/admin/stores
```java
POST   /                    → createStore
GET    /                    → getStores
GET    /{storeId}           → getStore
PUT    /{storeId}           → updateStore
DELETE /{storeId}           → deleteStore
POST   /{storeId}/tables    → setupTable
GET    /{storeId}/tables    → getTables
```

### AdminTableController - /api/admin/tables
```java
POST   /{tableId}/end-session    → endSession
GET    /{tableId}/order-history  → getOrderHistory
```
