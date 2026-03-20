# Unit 3-BE Menu API - Domain Entities

## Category (카테고리) — 신규

### 테이블: categories (init.sql에 이미 존재)
| 필드 | 타입 | 설명 |
|------|------|------|
| id | BIGINT PK | 카테고리 ID |
| store_id | BIGINT FK | 매장 ID |
| name | VARCHAR(50) | 카테고리명 |
| display_order | INT | 노출 순서 |

## Menu (메뉴) — 신규

### 테이블: menus (init.sql에 이미 존재)
| 필드 | 타입 | 설명 |
|------|------|------|
| id | BIGINT PK | 메뉴 ID |
| store_id | BIGINT FK | 매장 ID |
| category_id | BIGINT FK | 카테고리 ID |
| name | VARCHAR(100) | 메뉴명 |
| price | INT | 가격 (0 이상) |
| description | TEXT NULL | 설명 |
| image_url | VARCHAR(500) NULL | 이미지 URL |
| is_available | BOOLEAN | 판매 가능 여부 (Soft Delete용) |
| display_order | INT | 노출 순서 |
| created_at | DATETIME | 생성일 |
| updated_at | DATETIME | 수정일 |

### DB 스키마 변경
- menus 테이블에 `is_available BOOLEAN NOT NULL DEFAULT TRUE` 컬럼 추가 필요 (init.sql에 없음)
