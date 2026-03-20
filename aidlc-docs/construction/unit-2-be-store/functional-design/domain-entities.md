# Domain Entities - Unit 2-BE (Store/Table)

## 1. Store Entity (Unit 1에서 생성 완료 - 수정 필요)

**수정 사항**: updatedAt 자동 갱신 지원 추가

## 2. StoreTable Entity (Unit 1에서 생성 완료 - 수정 없음)

## 3. TableSession Entity (신규)

**테이블**: `table_sessions`

| 필드 | 타입 | 제약조건 | 설명 |
|------|------|---------|------|
| id | Long | PK, AUTO_INCREMENT | 세션 ID |
| tableId | Long | FK → store_tables(id), NOT NULL | 테이블 ID |
| startedAt | LocalDateTime | NOT NULL, DEFAULT NOW() | 세션 시작 시각 |
| endedAt | LocalDateTime | NULL | 세션 종료 시각 (null=활성) |

**비즈니스 메서드**:
- `end()`: endedAt = now(), 세션 종료
- `isActive()`: endedAt == null
