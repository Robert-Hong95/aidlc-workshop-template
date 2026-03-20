# Business Logic Model - Unit 7-FE (Menu UI)

## 1. 관리자 — 메뉴 관리

### 카테고리 관리
1. 카테고리 목록 조회: GET /api/admin/categories?storeId={}
2. 카테고리 추가: POST /api/admin/categories (이름, 순서)
3. 카테고리 수정: PUT /api/admin/categories/{id}
4. 카테고리 삭제: DELETE /api/admin/categories/{id} (하위 메뉴 있으면 경고)
5. 카테고리 순서 변경: PUT /api/admin/categories/reorder (드래그 앤 드롭)

### 메뉴 CRUD
1. 메뉴 목록 조회 (카테고리별): GET /api/admin/menus?categoryId={}
2. 메뉴 등록: 이미지 업로드(POST /api/files) → 메뉴 저장(POST /api/admin/menus)
3. 메뉴 수정: PUT /api/admin/menus/{id}
4. 메뉴 삭제: DELETE /api/admin/menus/{id} (확인 팝업)
5. 메뉴 순서 변경: PUT /api/admin/menus/reorder (드래그 앤 드롭 @dnd-kit)

### 이미지 업로드
1. 파일 선택 → FileReader로 로컬 미리보기
2. 폼 제출 시 서버 업로드 (POST /api/files, multipart/form-data)
3. 서버 응답 URL을 메뉴 imageUrl에 저장

## 2. 고객 — 메뉴 조회

### 메뉴 화면
1. 카테고리 + 메뉴 조회: GET /api/customer/menus?storeId={}
2. 상단 가로 스크롤 카테고리 탭
3. 탭 클릭 시 해당 카테고리 메뉴로 스크롤
4. 메뉴 카드: 이미지, 이름, 가격, 설명, 추가 버튼
5. 메뉴 없을 시 EmptyState 표시
