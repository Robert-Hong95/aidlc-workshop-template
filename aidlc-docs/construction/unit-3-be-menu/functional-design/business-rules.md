# Business Rules - Unit 3-BE (Menu API)

## 가격 검증
- 최소: 100원, 최대: 1,000,000원
- 정수만 허용

## 카테고리 삭제 규칙
- 해당 카테고리에 메뉴(deleted=false)가 존재하면 삭제 차단 → CATEGORY_HAS_MENUS 에러

## 메뉴 삭제 규칙
- Soft delete: deleted 플래그 true 설정
- 고객 조회 시 deleted=true 메뉴 제외
- 관리자 조회 시에도 deleted=true 제외 (삭제된 메뉴는 보이지 않음)

## 이미지 업로드 규칙
- 허용 확장자: .jpg, .jpeg, .png
- Content-Type 검증: image/jpeg, image/png
- 최대 파일 크기: 5MB (5,242,880 bytes)
- 파일명: UUID.확장자 (충돌 방지)

## 순서 변경 규칙
- 메뉴/카테고리 ID 배열 순서대로 displayOrder = 0, 1, 2, ... 부여
- 전달된 ID가 해당 storeId에 속하지 않으면 무시

## DB 스키마 변경
- menus 테이블에 `deleted` BOOLEAN NOT NULL DEFAULT FALSE 컬럼 추가 필요
