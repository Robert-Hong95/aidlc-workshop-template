# TDD Code Generation Plan - Unit 3-BE (Menu API)

## Unit Context
- **Workspace Root**: /Users/wd-il000534/IdeaProjects/aidlc-workshop-template
- **Backend Root**: table-order/backend/
- **Project Type**: Greenfield
- **Stories**: US-A04, US-C02, US-S01

## Plan Step 0: Skeleton Generation
- [ ] DB 스키마 수정 (menus.deleted 컬럼 추가)
- [ ] Category entity
- [ ] Menu entity (deleted 필드 포함)
- [ ] CategoryRepository, MenuRepository
- [ ] DTOs: CategoryCreateRequest, CategoryResponse, MenuCreateRequest, MenuUpdateRequest, MenuResponse, CategoryWithMenusResponse, DisplayOrderRequest
- [ ] MenuService stub
- [ ] FileStorageService stub
- [ ] AdminMenuController, CustomerMenuController, FileController stubs
- [ ] ErrorCode 추가: CATEGORY_HAS_MENUS, FILE_SIZE_EXCEEDED
- [ ] Verify compilation

## Plan Step 1: MenuService TDD - Category (TC-MENU-001~006)
- [ ] RED/GREEN: TC-MENU-001 createCategory
- [ ] RED/GREEN: TC-MENU-002 getCategories
- [ ] RED/GREEN: TC-MENU-003 updateCategory
- [ ] RED/GREEN: TC-MENU-004 deleteCategory 성공
- [ ] RED/GREEN: TC-MENU-005 deleteCategory 실패
- [ ] RED/GREEN: TC-MENU-006 updateCategoryOrder
- [ ] VERIFY: 6 tests pass

## Plan Step 2: MenuService TDD - Menu (TC-MENU-007~015)
- [ ] RED/GREEN: TC-MENU-007 createMenu
- [ ] RED/GREEN: TC-MENU-008 createMenu 가격 에러
- [ ] RED/GREEN: TC-MENU-009 getMenus
- [ ] RED/GREEN: TC-MENU-010 updateMenu
- [ ] RED/GREEN: TC-MENU-011 deleteMenu (soft)
- [ ] RED/GREEN: TC-MENU-012 updateMenuOrder
- [ ] RED/GREEN: TC-MENU-013 getMenusForCustomer
- [ ] RED/GREEN: TC-MENU-014 getMenusForCustomer 빈 결과
- [ ] RED/GREEN: TC-MENU-015 createMenu 카테고리 없음
- [ ] VERIFY: 15 tests pass

## Plan Step 3: FileStorageService TDD (TC-FILE-001~002)
- [ ] RED/GREEN: TC-FILE-001 upload 성공
- [ ] RED/GREEN: TC-FILE-002 upload 잘못된 형식
- [ ] VERIFY: 2 tests pass

## Plan Step 4: Controller Layer TDD (TC-CTRL-001~008)
- [ ] Write all controller tests
- [ ] Implement AdminMenuController
- [ ] Implement CustomerMenuController
- [ ] Implement FileController
- [ ] VERIFY: 8 tests pass

## Plan Step 5: Documentation
- [ ] Create code-summary.md
- [ ] Update test-plan.md statuses
- [ ] Update aidlc-state.md
- [ ] Update audit.md
