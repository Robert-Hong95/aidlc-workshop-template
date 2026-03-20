# Unit 3-BE Menu API - Business Logic Model

## 1. 카테고리 CRUD

```
createCategory(storeId, name) → CategoryResponse
  1. Store 존재 확인
  2. 동일 매장 내 카테고리명 중복 확인
  3. displayOrder = 기존 최대값 + 1
  4. Category 저장

getCategories(storeId) → List<CategoryResponse>
  1. displayOrder 순 조회

updateCategory(categoryId, name) → CategoryResponse
  1. Category 존재 확인
  2. 이름 변경 후 저장

deleteCategory(categoryId) → void
  1. Category 존재 확인
  2. 해당 카테고리에 메뉴 있으면 예외 (CATEGORY_HAS_MENUS)
  3. 삭제
```

## 2. 메뉴 CRUD

```
createMenu(storeId, MenuCreateRequest) → MenuResponse
  1. Store 존재 확인
  2. Category 존재 확인
  3. 가격 검증 (0 이상)
  4. displayOrder = 기존 최대값 + 1
  5. Menu 저장 (is_available = true)

updateMenu(menuId, MenuUpdateRequest) → MenuResponse
  1. Menu 존재 확인
  2. 변경 필드 업데이트

deleteMenu(menuId) → void
  1. Menu 존재 확인
  2. Soft Delete: is_available = false

getAllMenus(storeId) → List<MenuResponse>
  1. 매장의 전체 메뉴 조회 (is_available=true만)

getMenusByCategory(storeId, categoryId) → List<MenuResponse>
  1. 카테고리별 메뉴 조회 (is_available=true만)

updateMenuOrder(storeId, List<MenuOrderItem>) → void
  1. 각 menuId의 displayOrder 일괄 업데이트
```

## 3. 고객용 메뉴 조회

```
getMenusForCustomer(storeId) → List<CategoryWithMenusResponse>
  1. 카테고리 목록 조회 (displayOrder 순)
  2. 각 카테고리별 메뉴 조회 (is_available=true, displayOrder 순)
  3. 카테고리+메뉴 중첩 구조로 반환
```

## 4. 파일 업로드

```
uploadFile(MultipartFile) → String (URL)
  1. 파일 형식 검증 (JPG/PNG만)
  2. 파일 크기 검증 (5MB 이하)
  3. UUID 파일명 생성
  4. 로컬 파일 시스템 저장
  5. 접근 URL 반환

getFile(filename) → Resource
  1. 파일 존재 확인
  2. Resource 반환
```

## 5. DTO

```java
// Request
record CategoryCreateRequest(@NotBlank String name) {}
record CategoryUpdateRequest(@NotBlank String name) {}
record MenuCreateRequest(@NotBlank String name, @NotNull @Min(0) Integer price,
    String description, @NotNull Long categoryId, String imageUrl) {}
record MenuUpdateRequest(String name, Integer price, String description,
    Long categoryId, String imageUrl) {}
record MenuOrderItem(@NotNull Long menuId, @NotNull Integer displayOrder) {}

// Response
record CategoryResponse(Long id, String name, int displayOrder) {}
record MenuResponse(Long id, String name, int price, String description,
    String imageUrl, Long categoryId, int displayOrder) {}
record CategoryWithMenusResponse(Long id, String name, List<MenuResponse> menus) {}
```

## 6. API 엔드포인트

| Method | Path | Auth | 설명 |
|--------|------|------|------|
| POST | `/api/admin/stores/{storeId}/categories` | ADMIN | 카테고리 생성 |
| GET | `/api/admin/stores/{storeId}/categories` | ADMIN | 카테고리 목록 |
| PUT | `/api/admin/categories/{categoryId}` | ADMIN | 카테고리 수정 |
| DELETE | `/api/admin/categories/{categoryId}` | ADMIN | 카테고리 삭제 |
| POST | `/api/admin/stores/{storeId}/menus` | ADMIN | 메뉴 등록 |
| GET | `/api/admin/stores/{storeId}/menus` | ADMIN | 메뉴 목록 |
| PUT | `/api/admin/menus/{menuId}` | ADMIN | 메뉴 수정 |
| DELETE | `/api/admin/menus/{menuId}` | ADMIN | 메뉴 삭제 (Soft) |
| PUT | `/api/admin/stores/{storeId}/menus/order` | ADMIN | 메뉴 순서 변경 |
| GET | `/api/customer/stores/{storeId}/menus` | TABLE | 고객용 메뉴 조회 |
| POST | `/api/files/upload` | ADMIN | 이미지 업로드 |
| GET | `/api/files/{filename}` | 불필요 | 이미지 조회 |
