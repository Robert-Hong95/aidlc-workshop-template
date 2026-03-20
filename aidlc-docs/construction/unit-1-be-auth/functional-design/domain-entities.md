# Domain Entities - Unit 1-BE (Auth)

## 1. Admin Entity

**테이블**: `admins`

| 필드 | 타입 | 제약조건 | 설명 |
|------|------|---------|------|
| id | Long | PK, AUTO_INCREMENT | 관리자 ID |
| storeId | Long | FK → stores(id), NOT NULL | 소속 매장 |
| username | String | NOT NULL, UNIQUE(store_id, username) | 사용자명 |
| password | String | NOT NULL | bcrypt 해싱된 비밀번호 |
| createdAt | LocalDateTime | NOT NULL, DEFAULT NOW() | 생성 시각 |

**JPA 매핑**:
- `@Entity`, `@Table(name = "admins")`
- `storeId`는 `@Column`으로 매핑 (Store 엔티티와 연관관계 없이 ID만 보유)

## 2. Store Entity (Auth에서 참조)

**테이블**: `stores`

| 필드 | 타입 | 제약조건 | 설명 |
|------|------|---------|------|
| id | Long | PK, AUTO_INCREMENT | 매장 ID |
| storeCode | String | NOT NULL, UNIQUE | 매장 식별 코드 |
| name | String | NOT NULL | 매장명 |
| createdAt | LocalDateTime | NOT NULL | 생성 시각 |
| updatedAt | LocalDateTime | NOT NULL | 수정 시각 |

**참고**: Store 엔티티는 Unit 2에서 본격 구현. Auth에서는 storeCode로 매장 조회 용도로만 사용.

## 3. StoreTable Entity (Auth에서 참조)

**테이블**: `store_tables`

| 필드 | 타입 | 제약조건 | 설명 |
|------|------|---------|------|
| id | Long | PK, AUTO_INCREMENT | 테이블 ID |
| storeId | Long | FK → stores(id), NOT NULL | 소속 매장 |
| tableNo | Integer | NOT NULL, UNIQUE(store_id, table_no) | 테이블 번호 |
| password | String | NOT NULL | bcrypt 해싱된 비밀번호 |
| createdAt | LocalDateTime | NOT NULL | 생성 시각 |

**참고**: StoreTable 엔티티는 Unit 2에서 본격 구현. Auth에서는 테이블 인증 용도로만 사용.
