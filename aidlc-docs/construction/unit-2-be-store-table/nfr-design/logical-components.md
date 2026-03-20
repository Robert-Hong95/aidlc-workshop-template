# Unit 2-BE Store/Table API - Logical Components

## 컴포넌트 구조

```
store/
├── domain/
│   ├── Store.java              (확장: createdAt, updatedAt)
│   └── StoreTable.java         (확장: createdAt)
├── repository/
│   ├── StoreRepository.java    (확장: existsByStoreCode)
│   └── StoreTableRepository.java (확장: findAllByStoreIdWithSession)
├── service/
│   └── StoreService.java       (신규: createStore, getStore, getStores)
├── controller/
│   └── AdminStoreController.java (신규)
└── dto/
    ├── StoreCreateRequest.java
    └── StoreResponse.java

table/
├── domain/
│   └── TableSession.java       (신규)
├── repository/
│   ├── TableSessionRepository.java (신규)
│   └── OrderHistoryRepository.java (신규)
├── service/
│   └── TableService.java       (신규: setupTable, getTables, startSession, endSession, getOrderHistory)
├── controller/
│   └── AdminTableController.java (신규)
└── dto/
    ├── TableSetupRequest.java
    ├── TableResponse.java
    ├── OrderHistoryResponse.java
    └── OrderHistoryPage.java

common/
├── config/
│   └── RateLimitInterceptor.java (신규)
└── domain/
    └── OrderHistory.java        (신규)
```

## 컴포넌트 간 의존성

```
AdminStoreController → StoreService → StoreRepository, AdminRepository
AdminTableController → TableService → StoreTableRepository, TableSessionRepository, OrderHistoryRepository
RateLimitInterceptor → (독립, WebMvcConfigurer에서 등록)
```

## 기존 컴포넌트 수정 사항

| 파일 | 변경 내용 |
|------|----------|
| SecurityConfig.java | POST /api/stores를 permitAll에 추가 |
| init.sql | table_sessions, order_history 테이블 추가 (이미 존재하면 확인) |
| WebMvcConfigurer | RateLimitInterceptor 등록 |
