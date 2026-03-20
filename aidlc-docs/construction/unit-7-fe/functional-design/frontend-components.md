# Frontend Components - Unit 7-FE (Menu UI)

## 1. 관리자앱 — 메뉴 관리

### MenuManagePage (app/menus/page.tsx)
- CategoryManager + MenuList + MenuForm

### CategoryManager
- Props: `categories`, `onAdd`, `onEdit`, `onDelete`, `onReorder`
- 카테고리 목록 + 추가/수정/삭제 + 드래그 순서 변경

### MenuList
- Props: `menus`, `onEdit`, `onDelete`, `onReorder`
- 카테고리별 메뉴 목록 + 드래그 순서 변경

### MenuItemRow
- Props: `menu`, `onEdit`, `onDelete`
- 메뉴 행 (이름, 가격, 이미지 썸네일, 수정/삭제 버튼)

### MenuForm
- Props: `menu?`, `categories`, `onSubmit`, `isLoading`, `onCancel`
- react-hook-form + zod: 메뉴명, 가격(100~1,000,000), 카테고리, 설명, 이미지
- ImageUpload 컴포넌트 포함

### ImageUpload
- Props: `value?`, `onChange`
- 파일 선택 → FileReader 로컬 미리보기
- 허용: jpg/jpeg/png/webp, 최대 5MB

## 2. 고객앱 — 메뉴 조회

### MenuPage (app/page.tsx — 기본 화면)
- CategoryTabs + MenuGrid

### CategoryTabs
- Props: `categories`, `activeId`, `onSelect`
- 상단 고정 가로 스크롤 탭

### MenuGrid
- Props: `menus`
- 메뉴 카드 그리드

### MenuCard
- Props: `menu`, `onAdd`
- 이미지 + 이름 + 가격 + 설명 + 추가 버튼

## 3. Hooks

### useMenus (admin)
- `menus`, `categories`, `createMenu`, `updateMenu`, `deleteMenu`, `reorderMenus`

### useCategories (admin)
- `categories`, `createCategory`, `updateCategory`, `deleteCategory`, `reorderCategories`

### useCustomerMenu (customer)
- `menuData` (카테고리+메뉴 통합), `isLoading`

## 4. API 연동 포인트

| 컴포넌트 | Endpoint | Method |
|---------|----------|--------|
| CategoryManager | /api/admin/categories | GET/POST/PUT/DELETE |
| CategoryManager | /api/admin/categories/reorder | PUT |
| MenuList | /api/admin/menus?categoryId={} | GET |
| MenuForm | /api/admin/menus | POST/PUT |
| MenuForm | /api/files | POST (multipart) |
| MenuItemRow | /api/admin/menus/{id} | DELETE |
| MenuList | /api/admin/menus/reorder | PUT |
| MenuPage (customer) | /api/customer/menus?storeId={} | GET |
