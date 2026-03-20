# Component Methods - 테이블오더 서비스

> 상세 비즈니스 규칙은 Functional Design(Construction Phase)에서 정의합니다.

---

## 1. Service Layer Methods

### AuthService
| Method | Input | Output | Purpose |
|--------|-------|--------|---------|
| loginAdmin(storeCode, username, password) | LoginRequest | TokenResponse | 관리자 로그인, JWT 발급 |
| loginTable(storeCode, tableNo, password) | TableLoginRequest | TokenResponse | 테이블 인증, JWT 발급 |
| validateToken(token) | String | AuthInfo | JWT 토큰 검증 |

### StoreService
| Method | Input | Output | Purpose |
|--------|-------|--------|---------|
| createStore(request) | StoreCreateRequest | Store | 매장 등록 |
| getStore(storeId) | Long | Store | 매장 조회 |
| getStores() | - | List\<Store\> | 매장 목록 조회 |

### TableService
| Method | Input | Output | Purpose |
|--------|-------|--------|---------|
| setupTable(storeId, request) | Long, TableSetupRequest | StoreTable | 테이블 초기 설정 |
| getTables(storeId) | Long | List\<StoreTable\> | 매장 테이블 목록 |
| startSession(tableId) | Long | TableSession | 테이블 세션 시작 |
| endSession(tableId) | Long | void | 테이블 이용 완료 (주문→이력 이동, 리셋) |
| getActiveSession(tableId) | Long | TableSession | 현재 활성 세션 조회 |

### MenuService
| Method | Input | Output | Purpose |
|--------|-------|--------|---------|
| getCategories(storeId) | Long | List\<Category\> | 카테고리 목록 조회 |
| getMenusByCategory(storeId, categoryId) | Long, Long | List\<Menu\> | 카테고리별 메뉴 조회 |
| getAllMenus(storeId) | Long | List\<Menu\> | 전체 메뉴 조회 |
| createMenu(storeId, request) | Long, MenuCreateRequest | Menu | 메뉴 등록 |
| updateMenu(menuId, request) | Long, MenuUpdateRequest | Menu | 메뉴 수정 |
| deleteMenu(menuId) | Long | void | 메뉴 삭제 |
| updateMenuOrder(storeId, request) | Long, MenuOrderRequest | void | 메뉴 노출 순서 변경 |

### OrderService
| Method | Input | Output | Purpose |
|--------|-------|--------|---------|
| createOrder(storeId, tableId, request) | Long, Long, OrderCreateRequest | Order | 주문 생성 + SSE 이벤트 발행 |
| getOrdersBySession(sessionId) | Long | List\<Order\> | 세션별 주문 조회 |
| getActiveOrdersByStore(storeId) | Long | List\<Order\> | 매장 활성 주문 조회 |
| updateOrderStatus(orderId, status) | Long, OrderStatus | Order | 주문 상태 변경 + SSE 이벤트 발행 |
| deleteOrder(orderId) | Long | void | 주문 삭제 + SSE 이벤트 발행 |
| getOrderHistory(tableId, dateFrom, dateTo) | Long, LocalDate, LocalDate | List\<OrderHistory\> | 과거 주문 내역 조회 |

### SseEmitterService
| Method | Input | Output | Purpose |
|--------|-------|--------|---------|
| subscribe(storeId, clientType, clientId) | Long, ClientType, String | SseEmitter | SSE 연결 등록 |
| publishToStore(storeId, event) | Long, SseEvent | void | 매장 관리자에게 이벤트 전송 |
| publishToTable(storeId, tableId, event) | Long, Long, SseEvent | void | 특정 테이블에 이벤트 전송 |

### FileStorageService
| Method | Input | Output | Purpose |
|--------|-------|--------|---------|
| uploadFile(file) | MultipartFile | String (URL) | 파일 업로드, URL 반환 |
| getFile(filename) | String | Resource | 파일 조회 |

---

## 2. Repository Layer Methods

### StoreRepository
| Method | Purpose |
|--------|---------|
| findByStoreCode(code) | 매장 코드로 조회 |

### TableRepository (StoreTableRepository)
| Method | Purpose |
|--------|---------|
| findByStoreIdAndTableNo(storeId, tableNo) | 매장+테이블번호로 조회 |
| findAllByStoreId(storeId) | 매장별 테이블 목록 |

### TableSessionRepository
| Method | Purpose |
|--------|---------|
| findActiveByTableId(tableId) | 활성 세션 조회 |

### MenuRepository
| Method | Purpose |
|--------|---------|
| findByStoreIdAndCategoryIdOrderByDisplayOrder(storeId, categoryId) | 카테고리별 정렬 조회 |
| findAllByStoreIdOrderByCategoryDisplayOrderAscDisplayOrderAsc(storeId) | 전체 메뉴 정렬 조회 |

### CategoryRepository
| Method | Purpose |
|--------|---------|
| findAllByStoreIdOrderByDisplayOrder(storeId) | 매장별 카테고리 정렬 조회 |

### OrderRepository
| Method | Purpose |
|--------|---------|
| findBySessionIdOrderByCreatedAtDesc(sessionId) | 세션별 주문 조회 |
| findByStoreIdAndStatusIn(storeId, statuses) | 매장 활성 주문 조회 |

### OrderHistoryRepository
| Method | Purpose |
|--------|---------|
| findByTableIdAndCompletedAtBetween(tableId, from, to) | 기간별 과거 내역 조회 |
