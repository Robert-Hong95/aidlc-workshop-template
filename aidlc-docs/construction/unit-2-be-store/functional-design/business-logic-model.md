# Business Logic Model - Unit 2-BE (Store/Table)

## 1. 매장 등록
```
POST /api/admin/stores
Request: { storeCode, name }
1. StoreRepository.findByStoreCode(storeCode) → 이미 존재하면 DUPLICATE_STORE_CODE
2. StoreRepository.save(new Store(storeCode, name))
3. Return: { id, storeCode, name }
```

## 2. 매장 목록 조회
```
GET /api/admin/stores
1. StoreRepository.findAll()
2. Return: List<StoreResponse>
```

## 3. 매장 단건 조회
```
GET /api/admin/stores/{storeId}
1. StoreRepository.findById(storeId) → 없으면 STORE_NOT_FOUND
2. Return: StoreResponse
```

## 4. 매장 수정
```
PUT /api/admin/stores/{storeId}
Request: { name }
1. StoreRepository.findById(storeId) → 없으면 STORE_NOT_FOUND
2. store.updateName(name)
3. Return: StoreResponse
```

## 5. 매장 삭제
```
DELETE /api/admin/stores/{storeId}
1. StoreRepository.findById(storeId) → 없으면 STORE_NOT_FOUND
2. StoreRepository.delete(store)
3. Return: void
```

## 6. 테이블 초기 설정
```
POST /api/admin/stores/{storeId}/tables
Request: { tableNo, password }
1. StoreRepository.findById(storeId) → 없으면 STORE_NOT_FOUND
2. StoreTableRepository.findByStoreIdAndTableNo(storeId, tableNo) → 이미 존재하면 DUPLICATE_TABLE_NO
3. PasswordEncoder.encode(password)
4. StoreTableRepository.save(new StoreTable(storeId, tableNo, encodedPassword))
5. Return: { id, storeId, tableNo }
```

## 7. 매장 테이블 목록 조회
```
GET /api/admin/stores/{storeId}/tables
1. StoreTableRepository.findAllByStoreId(storeId)
2. 각 테이블의 활성 세션 여부 포함
3. Return: List<TableResponse>
```

## 8. 테이블 세션 종료 (이용 완료)
```
POST /api/admin/tables/{tableId}/end-session
1. TableSessionRepository.findByTableIdAndEndedAtIsNull(tableId) → 없으면 SESSION_NOT_FOUND
2. OrderRepository.findBySessionId(session.id) → 세션 주문 조회
3. 각 주문을 JSON 직렬화 → OrderHistoryRepository.save()
4. OrderRepository.deleteAllBySessionId(session.id)
5. session.end()
6. Return: void
```

## 9. 과거 주문 내역 조회
```
GET /api/admin/tables/{tableId}/order-history?dateFrom=&dateTo=
1. OrderHistoryRepository.findByTableIdAndCompletedAtBetween(tableId, dateFrom, dateTo)
2. Return: List<OrderHistoryResponse>
```

## API 엔드포인트 정리

| Method | Path | 인증 | 설명 |
|--------|------|------|------|
| POST | /api/admin/stores | ADMIN | 매장 등록 |
| GET | /api/admin/stores | ADMIN | 매장 목록 |
| GET | /api/admin/stores/{storeId} | ADMIN | 매장 조회 |
| PUT | /api/admin/stores/{storeId} | ADMIN | 매장 수정 |
| DELETE | /api/admin/stores/{storeId} | ADMIN | 매장 삭제 |
| POST | /api/admin/stores/{storeId}/tables | ADMIN | 테이블 설정 |
| GET | /api/admin/stores/{storeId}/tables | ADMIN | 테이블 목록 |
| POST | /api/admin/tables/{tableId}/end-session | ADMIN | 세션 종료 |
| GET | /api/admin/tables/{tableId}/order-history | ADMIN | 과거 내역 |
