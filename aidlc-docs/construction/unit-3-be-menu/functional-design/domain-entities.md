# Domain Entities - Unit 3-BE (Menu API)

## Category
| Field | Type | 설명 |
|-------|------|------|
| id | Long | PK |
| storeId | Long | FK → stores |
| name | String | 카테고리명 |
| displayOrder | int | 노출 순서 |

## Menu
| Field | Type | 설명 |
|-------|------|------|
| id | Long | PK |
| storeId | Long | FK → stores |
| categoryId | Long | FK → categories |
| name | String | 메뉴명 |
| price | int | 가격 (100~1,000,000) |
| description | String | 설명 (nullable) |
| imageUrl | String | 이미지 URL (nullable) |
| displayOrder | int | 노출 순서 |
| deleted | boolean | Soft delete 플래그 |
| createdAt | LocalDateTime | 생성일시 |
| updatedAt | LocalDateTime | 수정일시 |

## API Endpoints

### Admin - Category
| Method | Path | 설명 |
|--------|------|------|
| POST | /api/admin/stores/{storeId}/categories | 카테고리 생성 |
| GET | /api/admin/stores/{storeId}/categories | 카테고리 목록 |
| PUT | /api/admin/categories/{categoryId} | 카테고리 수정 |
| DELETE | /api/admin/categories/{categoryId} | 카테고리 삭제 |
| PUT | /api/admin/stores/{storeId}/categories/order | 카테고리 순서 변경 |

### Admin - Menu
| Method | Path | 설명 |
|--------|------|------|
| POST | /api/admin/stores/{storeId}/menus | 메뉴 생성 |
| GET | /api/admin/stores/{storeId}/menus | 메뉴 목록 |
| PUT | /api/admin/menus/{menuId} | 메뉴 수정 |
| DELETE | /api/admin/menus/{menuId} | 메뉴 삭제 (soft) |
| PUT | /api/admin/stores/{storeId}/menus/order | 메뉴 순서 변경 |

### Customer - Menu
| Method | Path | 설명 |
|--------|------|------|
| GET | /api/customer/stores/{storeId}/menus | 카테고리+메뉴 통합 조회 |

### File
| Method | Path | 설명 |
|--------|------|------|
| POST | /api/files/upload | 이미지 업로드 |
| GET | /api/files/{filename} | 이미지 조회 |
