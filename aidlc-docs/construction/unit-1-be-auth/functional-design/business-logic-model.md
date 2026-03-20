# Business Logic Model - Unit 1-BE (Auth)

## 1. 관리자 로그인 플로우

```
POST /api/admin/auth/login
Request: { storeCode, username, password }

1. StoreRepository.findByStoreCode(storeCode)
   → 없으면: INVALID_CREDENTIALS (401)
2. AdminRepository.findByStoreIdAndUsername(store.id, username)
   → 없으면: INVALID_CREDENTIALS (401)
3. PasswordEncoder.matches(password, admin.password)
   → 불일치: INVALID_CREDENTIALS (401)
4. JwtTokenProvider.createToken(
     subject: admin.id.toString(),
     claims: { role: "ADMIN", storeId: store.id, username: admin.username }
   )
5. Return: { token, storeId, storeName, username }
```

## 2. 관리자 등록 플로우

```
POST /api/admin/auth/register
Request: { storeCode, username, password }

1. StoreRepository.findByStoreCode(storeCode)
   → 없으면: STORE_NOT_FOUND (404)
2. AdminRepository.findByStoreIdAndUsername(store.id, username)
   → 이미 존재: DUPLICATE_ADMIN (409)
3. PasswordEncoder.encode(password)
4. AdminRepository.save(new Admin(store.id, username, encodedPassword))
5. Return: { adminId, storeId, username }
```

## 3. 테이블 인증 플로우

```
POST /api/customer/auth/login
Request: { storeCode, tableNo, password }

1. StoreRepository.findByStoreCode(storeCode)
   → 없으면: INVALID_CREDENTIALS (401)
2. StoreTableRepository.findByStoreIdAndTableNo(store.id, tableNo)
   → 없으면: INVALID_CREDENTIALS (401)
3. PasswordEncoder.matches(password, table.password)
   → 불일치: INVALID_CREDENTIALS (401)
4. JwtTokenProvider.createToken(
     subject: table.id.toString(),
     claims: { role: "TABLE", storeId: store.id, tableId: table.id }
   )
   → 만료 시간: 없음 (Long.MAX_VALUE 또는 별도 설정)
5. Return: { token, storeId, storeName, tableId, tableNo }
```

## 4. 관리자 로그아웃

```
클라이언트 측에서만 처리:
- localStorage에서 토큰 삭제
- 서버 API 호출 불필요
```

## 5. API 엔드포인트 정리

| Method | Path | 인증 | 설명 |
|--------|------|------|------|
| POST | /api/admin/auth/login | 불필요 | 관리자 로그인 |
| POST | /api/admin/auth/register | 불필요 | 관리자 등록 |
| POST | /api/customer/auth/login | 불필요 | 테이블 인증 |
