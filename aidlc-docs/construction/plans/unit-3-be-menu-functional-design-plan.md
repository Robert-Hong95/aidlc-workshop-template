# Functional Design Plan - Unit 3-BE (Menu API)

## Unit 정보
- **Unit**: Unit 3-BE - 메뉴 관리 (Menu)
- **Stories**: US-A04 (메뉴 관리), US-C02 (메뉴 조회), US-S01 (이미지 업로드)
- **Services**: MenuService, FileStorageService
- **Controllers**: AdminMenuController, CustomerMenuController, FileController

## Plan Steps
- [x] Step 1: 비즈니스 질문 수집 및 답변
- [x] Step 2: Functional Design 문서 생성

---

## Step 1: 비즈니스 질문

### Q1. 카테고리 관리 범위
카테고리 CRUD를 이 Unit에서 구현합니까?
- A) 전체 CRUD (생성, 조회, 수정, 삭제, 순서 변경)
- B) 생성 + 조회만 (MVP)
- C) Seed data로만 관리 (API 없음)

[Answer]:A

### Q2. 메뉴 가격 유효 범위
메뉴 가격의 최소/최대 범위는?
- A) 100원 ~ 1,000,000원
- B) 0원 ~ 무제한 (검증 없음)
- C) 직접 지정: ___

[Answer]:A

### Q3. 이미지 업로드 제약
- 허용 파일 형식: A) JPG/PNG만  B) JPG/PNG/GIF/WebP
- 최대 파일 크기: A) 5MB  B) 10MB  C) 직접 지정: ___

[Answer]:A, A

### Q4. 메뉴 삭제 시 기존 주문 처리
이미 주문된 메뉴를 삭제할 때:
- A) Soft delete (is_deleted 플래그) - 기존 주문 참조 유지
- B) Hard delete - order_items에 menu_name/unit_price 이미 스냅샷되어 있으므로 문제 없음
- C) 주문이 있으면 삭제 차단

[Answer]: A

### Q5. 고객 메뉴 조회 API 구조
- A) 카테고리 + 메뉴를 한 번에 반환 (단일 API)
- B) 카테고리 목록 API + 카테고리별 메뉴 API (분리)

[Answer]: A

### Q6. 메뉴 순서 변경 방식
- A) 개별 메뉴의 displayOrder를 직접 수정 (PUT /menus/{id})
- B) 전체 순서를 한 번에 전송 (PUT /menus/order - 배열)

[Answer]: B

