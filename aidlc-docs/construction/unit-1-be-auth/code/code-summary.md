# Code Summary - Unit 1-BE (Auth API)

## 생성된 파일

### Domain Layer
- `auth/domain/Admin.java` - 관리자 엔티티 (admins 테이블)
- `store/domain/Store.java` - 매장 엔티티 (stores 테이블, Auth에서 참조)
- `store/domain/StoreTable.java` - 테이블 엔티티 (store_tables 테이블, Auth에서 참조)

### Repository Layer
- `auth/repository/AdminRepository.java` - findByStoreIdAndUsername
- `store/repository/StoreRepository.java` - findByStoreCode
- `store/repository/StoreTableRepository.java` - findByStoreIdAndTableNo

### DTO Layer
- `auth/dto/AdminLoginRequest.java` - 관리자 로그인 요청
- `auth/dto/AdminLoginResponse.java` - 관리자 로그인 응답 (token, storeId, storeName, username)
- `auth/dto/AdminRegisterRequest.java` - 관리자 등록 요청 (validation 포함)
- `auth/dto/AdminRegisterResponse.java` - 관리자 등록 응답
- `auth/dto/TableLoginRequest.java` - 테이블 인증 요청
- `auth/dto/TableLoginResponse.java` - 테이블 인증 응답 (token, storeId, storeName, tableId, tableNo)

### Service Layer
- `auth/service/AuthService.java` - loginAdmin, registerAdmin, loginTable

### Controller Layer
- `auth/controller/AdminAuthController.java` - POST /api/admin/auth/login, /register
- `auth/controller/TableAuthController.java` - POST /api/customer/auth/login

### 수정된 파일
- `common/exception/ErrorCode.java` - DUPLICATE_ADMIN 추가
- `src/main/resources/db/init.sql` - seed 데이터 추가 (매장, 관리자, 테이블)
- `build.gradle` - Java toolchain 21→17 (로컬 호환성)

### Test Files
- `auth/service/AuthServiceTest.java` - 11 tests (Unit Test, Mockito)
- `auth/controller/AdminAuthControllerTest.java` - 4 tests (Integration Test, MockMvc)
- `auth/controller/TableAuthControllerTest.java` - 2 tests (Integration Test, MockMvc)

## API 엔드포인트

| Method | Path | 인증 | 설명 |
|--------|------|------|------|
| POST | /api/admin/auth/login | 불필요 | 관리자 로그인 |
| POST | /api/admin/auth/register | 불필요 | 관리자 등록 |
| POST | /api/customer/auth/login | 불필요 | 테이블 인증 |

## 테스트 결과
- 총 17개 테스트 (Service 11 + Controller 6)
- 전체 통과
