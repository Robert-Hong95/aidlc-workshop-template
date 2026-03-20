# Domain Entities - Unit 4-BE (Order + SSE API)

## Order
| Field | Type | 설명 |
|-------|------|------|
| id | Long | PK |
| storeId | Long | FK → stores |
| tableId | Long | FK → store_tables |
| sessionId | Long | FK → table_sessions |
| totalAmount | int | 총 금액 |
| status | String | PENDING/PREPARING/COMPLETED |
| createdAt | LocalDateTime | |
| updatedAt | LocalDateTime | |

## OrderItem
| Field | Type | 설명 |
|-------|------|------|
| id | Long | PK |
| orderId | Long | FK → orders |
| menuId | Long | FK → menus |
| menuName | String | 스냅샷 |
| quantity | int | |
| unitPrice | int | 스냅샷 |

## API Endpoints

### Customer
| Method | Path | 설명 |
|--------|------|------|
| POST | /api/customer/stores/{storeId}/tables/{tableId}/orders | 주문 생성 |
| GET | /api/customer/stores/{storeId}/tables/{tableId}/orders | 세션 주문 조회 |
| DELETE | /api/customer/orders/{orderId} | 주문 취소 (PENDING만) |
| GET | /api/customer/stores/{storeId}/tables/{tableId}/sse | SSE 구독 |

### Admin
| Method | Path | 설명 |
|--------|------|------|
| GET | /api/admin/stores/{storeId}/orders | 매장 활성 주문 |
| PUT | /api/admin/orders/{orderId}/status | 상태 변경 |
| DELETE | /api/admin/orders/{orderId} | 주문 삭제 |
| GET | /api/admin/stores/{storeId}/sse | SSE 구독 |
