# Build and Test Summary

## Build Status
- **Build Tool**: Gradle 8.x (wrapper)
- **Java**: JDK 21
- **Framework**: Spring Boot 3.4.3
- **Build Status**: ✅ SUCCESS
- **Build Artifact**: `build/libs/table-order-0.0.1-SNAPSHOT.jar`

## Test Execution Summary

### Unit Tests
- **Total Tests**: 83
- **Passed**: 83
- **Failed**: 0
- **Status**: ✅ PASS
- **Approach**: TDD (Test-Driven Development) — 전 Unit 적용

### Unit별 테스트 분포
| Unit | Domain | Tests | Status |
|------|--------|-------|--------|
| Unit 1-BE | Auth (login, JWT, QR) | 27 | ✅ |
| Unit 2-BE | Store/Table (CRUD, session) | 21 | ✅ |
| Unit 3-BE | Menu (category, menu, file) | 17 | ✅ |
| Unit 4-BE | Order+SSE (order, status, SSE) | 17 | ✅ |
| Common | ApplicationTests | 1 | ✅ |

### Integration Tests
- **Test Scenarios**: 6개 시나리오 정의
- **Status**: 📋 수동 테스트 가이드 제공 (integration-test-instructions.md)

### Performance Tests
- **Status**: N/A (워크샵 프로젝트 — Rate Limiting만 구현)

### Additional Tests
- **Contract Tests**: N/A (단일 서비스)
- **Security Tests**: N/A (JWT + Rate Limiting 단위 테스트로 커버)
- **E2E Tests**: N/A (프론트엔드 별도)

## API Endpoints (28개)

| Category | Method | Endpoint | Auth |
|----------|--------|----------|------|
| Auth | POST | /api/admin/auth/login | - |
| Auth | POST | /api/admin/auth/logout | JWT |
| Auth | POST | /api/auth/refresh | Cookie |
| Auth | POST | /api/customer/auth/qr-token | JWT |
| Auth | POST | /api/customer/auth/qr-login | - |
| Store | POST | /api/stores | Rate Limited |
| Store | GET | /api/admin/stores | JWT |
| Store | GET | /api/admin/stores/{id} | JWT |
| Table | POST | /api/admin/stores/{id}/tables | JWT |
| Table | GET | /api/admin/stores/{id}/tables | JWT |
| Table | POST | /api/admin/tables/{id}/end-session | JWT |
| Table | GET | /api/admin/tables/{id}/order-history | JWT |
| Category | POST | /api/admin/categories | JWT |
| Category | GET | /api/admin/categories | JWT |
| Category | PUT | /api/admin/categories/{id} | JWT |
| Category | DELETE | /api/admin/categories/{id} | JWT |
| Menu | POST | /api/admin/menus | JWT |
| Menu | PUT | /api/admin/menus/{id} | JWT |
| Menu | DELETE | /api/admin/menus/{id} | JWT |
| Menu | PUT | /api/admin/menus/order | JWT |
| Menu | GET | /api/admin/menus/{id} | JWT |
| Menu | GET | /api/customer/stores/{id}/menus | - |
| File | POST | /api/files/upload | JWT |
| File | GET | /api/files/{filename} | - |
| Order | POST | /api/customer/stores/{sid}/tables/{tid}/orders | JWT |
| Order | GET | /api/customer/tables/{tid}/orders | JWT |
| Order | DELETE | /api/customer/orders/{id} | JWT |
| Order | GET | /api/admin/stores/{id}/orders | JWT |
| Order | PUT | /api/admin/orders/{id}/status | JWT |
| Order | DELETE | /api/admin/orders/{id} | JWT |
| SSE | GET | /api/customer/sse/subscribe | - |
| SSE | GET | /api/admin/sse/subscribe | - |

## Overall Status
- **Build**: ✅ SUCCESS
- **All Tests**: ✅ 83/83 PASS
- **Ready for Operations**: ✅ Yes
