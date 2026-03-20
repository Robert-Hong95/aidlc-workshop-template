# Business Logic Model - Unit 3-BE (Menu API)

## 1. 카테고리 관리 (전체 CRUD)
- 생성: storeId + name + displayOrder → Category
- 조회: storeId → List<Category> (displayOrder 정렬)
- 수정: categoryId + name/displayOrder 변경
- 삭제: 해당 카테고리에 메뉴가 있으면 삭제 차단
- 순서 변경: 카테고리 ID 배열 → displayOrder 일괄 업데이트

## 2. 메뉴 관리 (CRUD + 순서)
- 생성: storeId + categoryId + name + price + description? + imageUrl? + displayOrder → Menu
- 조회: storeId별 전체 메뉴 (category displayOrder → menu displayOrder 정렬)
- 수정: menuId + 변경 필드 → Menu
- 삭제: Soft delete (deleted = true), 고객 조회에서 제외
- 순서 변경: 메뉴 ID 배열 → displayOrder 일괄 업데이트

## 3. 고객 메뉴 조회
- 단일 API: storeId → 카테고리별 그룹핑된 메뉴 목록 (deleted 제외)
- 응답 구조: List<CategoryWithMenus> (category + menus[])

## 4. 이미지 업로드
- 허용: JPG, PNG만
- 최대 크기: 5MB
- 저장: 로컬 파일시스템 (uploads/ 디렉토리)
- 파일명: UUID + 원본 확장자
- 반환: /api/files/{filename} URL
