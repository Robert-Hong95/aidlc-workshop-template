# Test Plan - Unit 3-BE (Menu API)

## Unit Overview
- **Stories**: US-A04, US-C02, US-S01
- **Total Test Cases**: 25 (Service: 15, Controller: 8, FileStorage: 2)

## MenuService Tests (15)

### Category Operations
- **TC-MENU-001**: createCategory 성공
  - Given: 유효한 storeId, name
  - When: createCategory 호출
  - Then: CategoryResponse 반환
  - Story: US-A04 | Status: ⬜

- **TC-MENU-002**: getCategories 성공
  - Given: 매장에 카테고리 2개 존재
  - When: getCategories 호출
  - Then: displayOrder 정렬된 목록 반환
  - Story: US-A04 | Status: ⬜

- **TC-MENU-003**: updateCategory 성공
  - Given: 존재하는 categoryId
  - When: updateCategory 호출
  - Then: 수정된 CategoryResponse 반환
  - Story: US-A04 | Status: ⬜

- **TC-MENU-004**: deleteCategory 성공
  - Given: 메뉴 없는 카테고리
  - When: deleteCategory 호출
  - Then: 삭제 완료
  - Story: US-A04 | Status: ⬜

- **TC-MENU-005**: deleteCategory 실패 - 메뉴 존재
  - Given: 메뉴가 있는 카테고리
  - When: deleteCategory 호출
  - Then: CATEGORY_HAS_MENUS 에러
  - Story: US-A04 | Status: ⬜

- **TC-MENU-006**: updateCategoryOrder 성공
  - Given: 카테고리 ID 배열
  - When: updateCategoryOrder 호출
  - Then: displayOrder 업데이트
  - Story: US-A04 | Status: ⬜

### Menu Operations
- **TC-MENU-007**: createMenu 성공
  - Given: 유효한 storeId, categoryId, name, price
  - When: createMenu 호출
  - Then: MenuResponse 반환
  - Story: US-A04 | Status: ⬜

- **TC-MENU-008**: createMenu 실패 - 가격 범위 초과
  - Given: price = 0
  - When: createMenu 호출
  - Then: INVALID_PRICE 에러
  - Story: US-A04 | Status: ⬜

- **TC-MENU-009**: getMenus 성공
  - Given: 매장에 메뉴 존재
  - When: getMenus 호출
  - Then: deleted=false 메뉴만 반환
  - Story: US-A04 | Status: ⬜

- **TC-MENU-010**: updateMenu 성공
  - Given: 존재하는 menuId
  - When: updateMenu 호출
  - Then: 수정된 MenuResponse 반환
  - Story: US-A04 | Status: ⬜

- **TC-MENU-011**: deleteMenu 성공 (soft delete)
  - Given: 존재하는 menuId
  - When: deleteMenu 호출
  - Then: deleted=true 설정
  - Story: US-A04 | Status: ⬜

- **TC-MENU-012**: updateMenuOrder 성공
  - Given: 메뉴 ID 배열
  - When: updateMenuOrder 호출
  - Then: displayOrder 업데이트
  - Story: US-A04 | Status: ⬜

### Customer Query
- **TC-MENU-013**: getMenusForCustomer 성공
  - Given: 매장에 카테고리+메뉴 존재
  - When: getMenusForCustomer 호출
  - Then: 카테고리별 그룹핑된 메뉴 반환 (deleted 제외)
  - Story: US-C02 | Status: ⬜

- **TC-MENU-014**: getMenusForCustomer 빈 결과
  - Given: 메뉴 없는 매장
  - When: getMenusForCustomer 호출
  - Then: 빈 목록 반환
  - Story: US-C02 | Status: ⬜

### Error Cases
- **TC-MENU-015**: createMenu 실패 - 카테고리 없음
  - Given: 존재하지 않는 categoryId
  - When: createMenu 호출
  - Then: CATEGORY_NOT_FOUND 에러
  - Story: US-A04 | Status: ⬜

## FileStorageService Tests (2)

- **TC-FILE-001**: upload 성공
  - Given: 유효한 JPG 파일
  - When: upload 호출
  - Then: URL 문자열 반환
  - Story: US-S01 | Status: ⬜

- **TC-FILE-002**: upload 실패 - 잘못된 파일 형식
  - Given: .txt 파일
  - When: upload 호출
  - Then: INVALID_FILE_TYPE 에러
  - Story: US-S01 | Status: ⬜

## Controller Tests (8)

- **TC-CTRL-001**: POST /api/admin/stores/{storeId}/categories 성공
  - Story: US-A04 | Status: ⬜
- **TC-CTRL-002**: POST /api/admin/stores/{storeId}/menus 성공
  - Story: US-A04 | Status: ⬜
- **TC-CTRL-003**: DELETE /api/admin/menus/{menuId} 성공
  - Story: US-A04 | Status: ⬜
- **TC-CTRL-004**: GET /api/customer/stores/{storeId}/menus 성공
  - Story: US-C02 | Status: ⬜
- **TC-CTRL-005**: POST /api/files/upload 성공
  - Story: US-S01 | Status: ⬜
- **TC-CTRL-006**: POST /api/files/upload 실패 - 잘못된 형식
  - Story: US-S01 | Status: ⬜
- **TC-CTRL-007**: GET /api/files/{filename} 성공
  - Story: US-S01 | Status: ⬜
- **TC-CTRL-008**: PUT /api/admin/stores/{storeId}/menus/order 성공
  - Story: US-A04 | Status: ⬜

## Requirements Coverage
| Story | Test Cases | Status |
|-------|-----------|--------|
| US-A04 | TC-MENU-001~012,015, TC-CTRL-001~003,008 | ⬜ |
| US-C02 | TC-MENU-013~014, TC-CTRL-004 | ⬜ |
| US-S01 | TC-FILE-001~002, TC-CTRL-005~007 | ⬜ |
