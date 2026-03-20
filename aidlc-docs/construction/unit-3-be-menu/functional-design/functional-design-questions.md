# Unit 3-BE Menu API - Functional Design 질문

Unit 3-BE (Menu API) 상세 비즈니스 로직 설계를 위한 질문입니다.
각 질문의 [Answer]: 뒤에 선택지 알파벳을 입력해주세요.

---

## Question 1
카테고리 관리 범위는?

A) CRUD 전체 (생성, 조회, 수정, 삭제)
B) 생성 + 조회만 (수정/삭제 불필요, MVP 단순화)
C) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 2
메뉴 가격 유효 범위는?

A) 100원 ~ 1,000,000원
B) 0원 이상 (무료 메뉴 허용), 상한 없음
C) 1원 이상, 10,000,000원 이하
D) Other (please describe after [Answer]: tag below)

[Answer]: B

## Question 3
메뉴 삭제 시 해당 메뉴가 포함된 기존 주문이 있을 경우 처리는?

A) Soft Delete (is_available 플래그) — 기존 주문 유지, 고객 화면에서만 숨김
B) Hard Delete — 기존 주문의 menu_name은 order_items에 이미 저장되어 있으므로 삭제 가능
C) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 4
이미지 업로드 제한은?

A) 파일 형식: JPG/PNG만, 최대 5MB
B) 파일 형식: JPG/PNG/GIF/WebP, 최대 10MB
C) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 5
고객용 메뉴 조회 API 구조는?

A) 단일 API: GET /api/customer/stores/{storeId}/menus — 카테고리+메뉴 전체 반환
B) 2단계: GET /api/customer/stores/{storeId}/categories → GET /api/customer/stores/{storeId}/menus?categoryId=
C) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 6
메뉴 노출 순서 변경 API 설계는?

A) 전체 순서 일괄 업데이트: PUT /api/admin/stores/{storeId}/menus/order — body에 [{menuId, displayOrder}] 배열
B) 개별 메뉴 순서 변경: PATCH /api/admin/menus/{menuId}/order — body에 {displayOrder}
C) Other (please describe after [Answer]: tag below)

[Answer]: A
