# Unit 2-BE Store/Table API - Domain Entities

## Store (매장) — Unit 1에서 최소 정의 → 확장

Unit 1에서 생성한 Store.java를 확장합니다.
- 추가 필드 없음 (Q2=A, 현재 스키마 유지)
- createdAt, updatedAt 추가

## StoreTable (테이블) — Unit 1에서 최소 정의 → 확장

Unit 1에서 생성한 StoreTable.java를 확장합니다.
- createdAt 추가

## TableSession (테이블 세션) — 신규

### 테이블: table_sessions
| 필드 | 타입 | 제약조건 | 설명 |
|------|------|---------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 세션 ID |
| table_id | BIGINT | FK(store_tables.id), NOT NULL | 테이블 ID |
| started_at | DATETIME | NOT NULL, DEFAULT NOW() | 세션 시작 |
| ended_at | DATETIME | NULL | 세션 종료 (NULL=활성) |

### JPA Entity
```java
@Entity @Table(name = "table_sessions")
public class TableSession {
    Long getId();
    Long getTableId();
    LocalDateTime getStartedAt();
    LocalDateTime getEndedAt();

    boolean isActive();           // endedAt == null
    void end();                   // endedAt = now
    static TableSession start(Long tableId);  // 팩토리 메서드
}
```

## 비즈니스 규칙
- 테이블당 활성 세션은 최대 1개
- 세션 시작: 고객 첫 주문 시 자동 (Unit 4-BE OrderService에서 호출)
- 세션 종료: 관리자가 "이용 완료" 클릭 시
