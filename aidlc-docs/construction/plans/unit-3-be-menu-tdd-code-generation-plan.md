# TDD Code Generation Plan for Unit 3-BE Menu API

## Unit Context
- **Stories**: US-A04 (메뉴 관리), US-C02 (메뉴 조회), US-S01 (이미지 업로드)

### Plan Step 0: Skeleton + DB Schema ✅
- [x] 0.1~0.7: 모든 스켈레톤 생성 완료

### Plan Step 1: Business Logic Layer (TDD) ✅
- [x] 1.1: MenuService — 카테고리 CRUD (6 tests)
- [x] 1.2: MenuService — 메뉴 CRUD (5 tests)
- [x] 1.3: MenuService — 고객용 메뉴 조회 (1 test)
- [x] 1.4: MenuService — 메뉴 순서 변경 (1 test + 1 fix)
- [x] 1.5: FileStorageService — 업로드/조회 (4 tests)

### Plan Step 2: API Layer ✅
- [x] 2.1: AdminMenuController 구현
- [x] 2.2: CustomerMenuController 구현
- [x] 2.3: FileController 구현
- [x] 2.4: SecurityConfig 수정
- [x] 2.5: code-generation-summary.md 생성
