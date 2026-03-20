# Unit 3-BE Menu API - NFR Design Patterns

## DP-MN-01: File Upload Pattern
- LocalFileStorageService: UUID 파일명, 로컬 저장
- Spring ResourceHttpRequestHandler로 정적 파일 서빙
- multipart 설정: max-file-size=5MB, max-request-size=5MB

## DP-MN-02: Soft Delete Pattern
- Menu.isAvailable 필드
- 고객 조회: WHERE is_available = true
- 관리자 조회: 전체 (삭제 포함)

## DP-MN-03: Nested Response Pattern
- CategoryWithMenusResponse: 카테고리 + 메뉴 리스트 중첩
- 단일 쿼리로 메뉴 전체 조회 후 Java에서 그룹핑
