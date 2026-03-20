# Unit 3-BE Menu API - Business Rules

## BR-MN-01: 카테고리 관리
- 매장 내 카테고리명 유일
- 카테고리 삭제 시 해당 카테고리에 메뉴가 있으면 삭제 불가 (CATEGORY_HAS_MENUS)
- displayOrder 자동 증가

## BR-MN-02: 메뉴 등록/수정
- 필수: name, price, categoryId
- 가격: 0 이상 (상한 없음)
- 카테고리 존재 확인 필수
- displayOrder 자동 증가

## BR-MN-03: 메뉴 삭제
- Soft Delete: is_available = false
- 고객 조회 시 is_available=true만 반환
- 관리자 조회 시 전체 반환 (삭제된 메뉴 포함)

## BR-MN-04: 메뉴 순서 변경
- 일괄 업데이트: [{menuId, displayOrder}] 배열
- 매장 소속 메뉴만 변경 가능

## BR-MN-05: 이미지 업로드
- 허용 형식: JPG, PNG
- 최대 크기: 5MB
- UUID 파일명으로 저장
- 로컬 파일 시스템 (application.yml의 upload-dir)

## BR-MN-06: 고객용 메뉴 조회
- 카테고리+메뉴 중첩 구조 반환
- is_available=true만 포함
- displayOrder 순 정렬
