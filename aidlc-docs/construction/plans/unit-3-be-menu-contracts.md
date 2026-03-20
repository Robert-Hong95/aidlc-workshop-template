# Contract/Interface Definition - Unit 3-BE (Menu API)

## Unit Context
- **Stories**: US-A04 (메뉴 관리), US-C02 (메뉴 조회), US-S01 (이미지 업로드)
- **Dependencies**: Unit 0 (common, security), Unit 2 (Store entity)
- **Database Entities**: Category, Menu (+ menus.deleted 컬럼 추가)

## Business Logic Layer

### MenuService
- `createCategory(storeId, request) -> CategoryResponse`: 카테고리 생성
  - Raises: STORE_NOT_FOUND
- `getCategories(storeId) -> List<CategoryResponse>`: 카테고리 목록 (displayOrder 정렬)
- `updateCategory(categoryId, request) -> CategoryResponse`: 카테고리 수정
  - Raises: CATEGORY_NOT_FOUND
- `deleteCategory(categoryId) -> void`: 카테고리 삭제
  - Raises: CATEGORY_NOT_FOUND, CATEGORY_HAS_MENUS
- `updateCategoryOrder(storeId, request) -> void`: 카테고리 순서 변경
- `createMenu(storeId, request) -> MenuResponse`: 메뉴 생성
  - Raises: CATEGORY_NOT_FOUND, INVALID_PRICE
- `getMenus(storeId) -> List<MenuResponse>`: 관리자 메뉴 목록 (deleted 제외)
- `updateMenu(menuId, request) -> MenuResponse`: 메뉴 수정
  - Raises: MENU_NOT_FOUND, INVALID_PRICE
- `deleteMenu(menuId) -> void`: 메뉴 soft delete
  - Raises: MENU_NOT_FOUND
- `updateMenuOrder(storeId, request) -> void`: 메뉴 순서 변경
- `getMenusForCustomer(storeId) -> List<CategoryWithMenusResponse>`: 고객용 카테고리+메뉴 통합

### FileStorageService
- `upload(file) -> String`: 파일 업로드, URL 반환
  - Raises: INVALID_FILE_TYPE, FILE_UPLOAD_FAILED
- `loadAsResource(filename) -> Resource`: 파일 조회

## API Layer

### AdminMenuController (/api/admin)
- `POST /stores/{storeId}/categories` → createCategory
- `GET /stores/{storeId}/categories` → getCategories
- `PUT /categories/{categoryId}` → updateCategory
- `DELETE /categories/{categoryId}` → deleteCategory
- `PUT /stores/{storeId}/categories/order` → updateCategoryOrder
- `POST /stores/{storeId}/menus` → createMenu
- `GET /stores/{storeId}/menus` → getMenus
- `PUT /menus/{menuId}` → updateMenu
- `DELETE /menus/{menuId}` → deleteMenu
- `PUT /stores/{storeId}/menus/order` → updateMenuOrder

### CustomerMenuController (/api/customer)
- `GET /stores/{storeId}/menus` → getMenusForCustomer

### FileController (/api/files)
- `POST /upload` → upload
- `GET /{filename}` → loadAsResource

## Repository Layer

### CategoryRepository
- `findAllByStoreIdOrderByDisplayOrder(storeId) -> List<Category>`
- `existsByIdAndStoreId(categoryId, storeId) -> boolean`

### MenuRepository
- `findAllByStoreIdAndDeletedFalseOrderByCategoryDisplayOrderAscDisplayOrderAsc(storeId) -> List<Menu>`
- `existsByCategoryIdAndDeletedFalse(categoryId) -> boolean`
