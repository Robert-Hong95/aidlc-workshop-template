# Domain Entities - Unit 7-FE (Menu UI)

### types/menu.ts
```typescript
export interface Category {
  id: number;
  storeId: number;
  name: string;
  sortOrder: number;
}

export interface Menu {
  id: number;
  categoryId: number;
  name: string;
  price: number;
  description?: string;
  imageUrl?: string;
  sortOrder: number;
}

export interface CreateMenuRequest {
  categoryId: number;
  name: string;
  price: number;
  description?: string;
  imageUrl?: string;
}

export interface UpdateMenuRequest {
  name?: string;
  price?: number;
  description?: string;
  imageUrl?: string;
  categoryId?: number;
}

export interface CreateCategoryRequest {
  storeId: number;
  name: string;
}

export interface ReorderRequest {
  ids: number[];
}

export interface CustomerMenuResponse {
  categories: (Category & { menus: Menu[] })[];
}
```
