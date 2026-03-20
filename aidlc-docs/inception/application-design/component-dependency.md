# Component Dependency - 테이블오더 서비스

## 1. Backend 의존성 매트릭스

### Controller → Service
| Controller | 의존 Service |
|------------|-------------|
| AdminAuthController | AuthService |
| TableAuthController | AuthService |
| AdminOrderController | OrderService |
| CustomerOrderController | OrderService, TableService |
| AdminTableController | TableService |
| AdminMenuController | MenuService |
| CustomerMenuController | MenuService |
| AdminStoreController | StoreService |
| AdminSseController | SseEmitterService |
| CustomerSseController | SseEmitterService |
| FileController | FileStorageService |

### Service → Service (Cross-Service 의존성)
| Service | 의존 Service | 이유 |
|---------|-------------|------|
| OrderService | SseEmitterService | 주문 이벤트 발행 |
| OrderService | TableService | 세션 확인/자동 시작 |
| TableService | OrderService | 이용 완료 시 주문 이력 이동 |

### Service → Repository
| Service | 의존 Repository |
|---------|----------------|
| AuthService | AdminRepository, StoreRepository, TableRepository |
| StoreService | StoreRepository |
| TableService | TableRepository, TableSessionRepository, OrderHistoryRepository |
| MenuService | MenuRepository, CategoryRepository |
| OrderService | OrderRepository, OrderItemRepository, TableSessionRepository |
| FileStorageService | (파일 시스템 직접 접근) |

---

## 2. Frontend 의존성

### 공유 패키지 의존성
```
apps/customer  → @table-order/ui, @table-order/api-client, @table-order/shared
apps/admin     → @table-order/ui, @table-order/api-client, @table-order/shared
```

### API Client → Backend API 매핑
| Frontend 모듈 | Backend API |
|---------------|-------------|
| customer/useTableAuth | /api/customer/auth/* |
| customer/useMenu | /api/customer/menus/* |
| customer/useCart | (로컬 저장소만) |
| customer/useOrder | /api/customer/orders/* |
| customer/useSSE | /api/customer/sse/* |
| admin/useAdminAuth | /api/admin/auth/* |
| admin/useOrders | /api/admin/orders/* |
| admin/useTables | /api/admin/tables/* |
| admin/useMenus | /api/admin/menus/* |
| admin/useStores | /api/admin/stores/* |
| admin/useSSE | /api/admin/sse/* |

---

## 3. 데이터 흐름

### 고객 주문 플로우
```
[Customer App] --POST /api/customer/orders--> [CustomerOrderController]
    --> [OrderService] --save--> [OrderRepository] --MySQL-->
    --> [SseEmitterService] --SSE event--> [Admin App]
```

### 관리자 상태 변경 플로우
```
[Admin App] --PATCH /api/admin/orders/{id}/status--> [AdminOrderController]
    --> [OrderService] --update--> [OrderRepository] --MySQL-->
    --> [SseEmitterService] --SSE event--> [Admin App] + [Customer App]
```

### 테이블 이용 완료 플로우
```
[Admin App] --POST /api/admin/tables/{id}/end-session--> [AdminTableController]
    --> [TableService]
        --> [OrderService] --move--> [OrderHistoryRepository] --MySQL-->
        --> [TableSessionRepository] --end session-->
    --> [SseEmitterService] --SSE event--> [Admin App]
```
