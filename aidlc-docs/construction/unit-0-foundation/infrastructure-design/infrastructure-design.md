# Infrastructure Design - Unit 0 (Foundation)

## 배포 환경
- **환경**: 로컬 개발 환경 (Docker Compose)
- **컨테이너**: Backend, Customer Frontend, Admin Frontend, MySQL

---

## Docker Compose 구성

### 서비스 구성
| 서비스 | 이미지/빌드 | 포트 | 설명 |
|--------|-----------|------|------|
| mysql | mysql:8.0 | 3306:3306 | MySQL 데이터베이스 |
| backend | ./backend (Dockerfile) | 8080:8080 | Spring Boot API |
| customer | ./frontend/apps/customer (Dockerfile) | 3000:3000 | 고객용 Next.js |
| admin | ./frontend/apps/admin (Dockerfile) | 3001:3001 | 관리자용 Next.js |

### MySQL 설정
- **Database**: table_order
- **Character Set**: utf8mb4
- **Collation**: utf8mb4_unicode_ci
- **Volume**: mysql-data (영속 저장)
- **Init Script**: docker-entrypoint-initdb.d/ 에 초기 스키마 배치

### 네트워크
- 단일 Docker 네트워크 (table-order-network)
- Backend → MySQL: 내부 네트워크 통신 (mysql:3306)
- Frontend → Backend: http://backend:8080

### 환경 변수
**Backend**:
- SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/table_order
- SPRING_DATASOURCE_USERNAME / PASSWORD
- JWT_SECRET, JWT_EXPIRATION
- FILE_UPLOAD_DIR=/app/uploads

**Frontend (공통)**:
- NEXT_PUBLIC_API_URL=http://localhost:8080

---

## MySQL 스키마 설계

### stores (매장)
| 컬럼 | 타입 | 제약조건 | 설명 |
|------|------|---------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 매장 ID |
| store_code | VARCHAR(50) | UNIQUE, NOT NULL | 매장 식별 코드 |
| name | VARCHAR(100) | NOT NULL | 매장명 |
| created_at | DATETIME | NOT NULL, DEFAULT NOW() | 생성일시 |
| updated_at | DATETIME | NOT NULL, DEFAULT NOW() | 수정일시 |

### admins (관리자)
| 컬럼 | 타입 | 제약조건 | 설명 |
|------|------|---------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 관리자 ID |
| store_id | BIGINT | FK(stores.id), NOT NULL | 매장 ID |
| username | VARCHAR(50) | NOT NULL | 사용자명 |
| password | VARCHAR(255) | NOT NULL | 비밀번호 (bcrypt) |
| created_at | DATETIME | NOT NULL, DEFAULT NOW() | 생성일시 |
| | | UNIQUE(store_id, username) | 매장 내 유일 |

### store_tables (테이블)
| 컬럼 | 타입 | 제약조건 | 설명 |
|------|------|---------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 테이블 ID |
| store_id | BIGINT | FK(stores.id), NOT NULL | 매장 ID |
| table_no | INT | NOT NULL | 테이블 번호 |
| password | VARCHAR(255) | NOT NULL | 테이블 비밀번호 (bcrypt) |
| created_at | DATETIME | NOT NULL, DEFAULT NOW() | 생성일시 |
| | | UNIQUE(store_id, table_no) | 매장 내 유일 |

### table_sessions (테이블 세션)
| 컬럼 | 타입 | 제약조건 | 설명 |
|------|------|---------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 세션 ID |
| table_id | BIGINT | FK(store_tables.id), NOT NULL | 테이블 ID |
| started_at | DATETIME | NOT NULL, DEFAULT NOW() | 세션 시작 |
| ended_at | DATETIME | NULL | 세션 종료 (NULL=활성) |

### categories (카테고리)
| 컬럼 | 타입 | 제약조건 | 설명 |
|------|------|---------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 카테고리 ID |
| store_id | BIGINT | FK(stores.id), NOT NULL | 매장 ID |
| name | VARCHAR(50) | NOT NULL | 카테고리명 |
| display_order | INT | NOT NULL, DEFAULT 0 | 노출 순서 |

### menus (메뉴)
| 컬럼 | 타입 | 제약조건 | 설명 |
|------|------|---------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 메뉴 ID |
| store_id | BIGINT | FK(stores.id), NOT NULL | 매장 ID |
| category_id | BIGINT | FK(categories.id), NOT NULL | 카테고리 ID |
| name | VARCHAR(100) | NOT NULL | 메뉴명 |
| price | INT | NOT NULL | 가격 (원) |
| description | TEXT | NULL | 설명 |
| image_url | VARCHAR(500) | NULL | 이미지 URL |
| display_order | INT | NOT NULL, DEFAULT 0 | 노출 순서 |
| created_at | DATETIME | NOT NULL, DEFAULT NOW() | 생성일시 |
| updated_at | DATETIME | NOT NULL, DEFAULT NOW() | 수정일시 |

### orders (주문)
| 컬럼 | 타입 | 제약조건 | 설명 |
|------|------|---------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 주문 ID |
| store_id | BIGINT | FK(stores.id), NOT NULL | 매장 ID |
| table_id | BIGINT | FK(store_tables.id), NOT NULL | 테이블 ID |
| session_id | BIGINT | FK(table_sessions.id), NOT NULL | 세션 ID |
| total_amount | INT | NOT NULL | 총 금액 |
| status | VARCHAR(20) | NOT NULL, DEFAULT 'PENDING' | 상태 (PENDING/PREPARING/COMPLETED) |
| created_at | DATETIME | NOT NULL, DEFAULT NOW() | 주문일시 |
| updated_at | DATETIME | NOT NULL, DEFAULT NOW() | 수정일시 |

### order_items (주문 항목)
| 컬럼 | 타입 | 제약조건 | 설명 |
|------|------|---------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 항목 ID |
| order_id | BIGINT | FK(orders.id), NOT NULL | 주문 ID |
| menu_id | BIGINT | FK(menus.id), NOT NULL | 메뉴 ID |
| menu_name | VARCHAR(100) | NOT NULL | 주문 시점 메뉴명 |
| quantity | INT | NOT NULL | 수량 |
| unit_price | INT | NOT NULL | 주문 시점 단가 |

### order_history (주문 이력)
| 컬럼 | 타입 | 제약조건 | 설명 |
|------|------|---------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 이력 ID |
| store_id | BIGINT | NOT NULL | 매장 ID |
| table_id | BIGINT | NOT NULL | 테이블 ID |
| session_id | BIGINT | NOT NULL | 세션 ID |
| order_data | JSON | NOT NULL | 주문 전체 데이터 (메뉴목록, 금액 등) |
| total_amount | INT | NOT NULL | 총 금액 |
| ordered_at | DATETIME | NOT NULL | 원래 주문일시 |
| completed_at | DATETIME | NOT NULL, DEFAULT NOW() | 이용 완료 시각 |

---

## Spring Boot 프로젝트 구조

```
backend/
├── src/main/java/com/tableorder/
│   ├── TableOrderApplication.java
│   ├── common/
│   │   ├── dto/ApiResponse.java              # 공통 API 응답 래퍼
│   │   ├── exception/                         # 글로벌 예외 처리
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   ├── BusinessException.java
│   │   │   └── ErrorCode.java
│   │   └── config/
│   │       ├── SecurityConfig.java
│   │       ├── JwtTokenProvider.java
│   │       ├── JwtAuthenticationFilter.java
│   │       ├── WebConfig.java                 # CORS 설정
│   │       └── SseConfig.java
│   ├── auth/
│   │   ├── domain/Admin.java
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   └── dto/
│   ├── store/
│   │   ├── domain/{Store, StoreTable, TableSession}.java
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   └── dto/
│   ├── menu/
│   │   ├── domain/{Menu, Category}.java
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   └── dto/
│   └── order/
│       ├── domain/{Order, OrderItem, OrderHistory}.java
│       ├── controller/
│       ├── service/
│       ├── repository/
│       └── dto/
├── src/main/resources/
│   ├── application.yml
│   └── db/migration/                          # 스키마 init
├── Dockerfile
└── build.gradle
```

## Turborepo 모노레포 구조

```
frontend/
├── apps/
│   ├── customer/
│   │   ├── app/                               # Next.js App Router
│   │   ├── components/
│   │   ├── hooks/
│   │   ├── stores/
│   │   ├── lib/
│   │   ├── types/
│   │   ├── next.config.ts
│   │   ├── tailwind.config.ts
│   │   ├── Dockerfile
│   │   └── package.json
│   └── admin/
│       ├── app/
│       ├── components/
│       ├── hooks/
│       ├── stores/
│       ├── lib/
│       ├── types/
│       ├── next.config.ts
│       ├── tailwind.config.ts
│       ├── Dockerfile
│       └── package.json
├── packages/
│   ├── ui/
│   │   ├── components/
│   │   ├── layouts/
│   │   ├── index.ts
│   │   └── package.json
│   ├── api-client/
│   │   ├── client.ts
│   │   ├── types/
│   │   └── package.json
│   └── shared/
│       ├── utils/
│       ├── constants/
│       ├── types/
│       └── package.json
├── turbo.json
├── pnpm-workspace.yaml
├── package.json
└── tsconfig.json                              # 공유 TS 설정
```
