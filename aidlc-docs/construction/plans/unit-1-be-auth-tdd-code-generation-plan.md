# TDD Code Generation Plan - Unit 1-BE (Auth API)

## Unit Context
- **Workspace Root**: /Users/wd-il000534/IdeaProjects/aidlc-workshop-template
- **Project Type**: Greenfield
- **Backend Root**: table-order/backend/
- **Source**: src/main/java/com/tableorder/
- **Test**: src/test/java/com/tableorder/
- **Stories**: US-A01 (매장 인증), US-C01 (테이블 자동 로그인)

---

## Plan Step 0: Contract Skeleton + DTO Generation
- [x] 0.1 Entity: Admin, Store, StoreTable (domain 클래스)
- [x] 0.2 Repository: AdminRepository, StoreRepository, StoreTableRepository
- [x] 0.3 DTO: AdminLoginRequest, AdminRegisterRequest, TableLoginRequest, AdminLoginResponse, AdminRegisterResponse, TableLoginResponse
- [x] 0.4 Service: AuthService (메서드 stub - throw UnsupportedOperationException)
- [x] 0.5 Controller: AdminAuthController, TableAuthController (메서드 stub)
- [x] 0.6 ErrorCode 추가: DUPLICATE_ADMIN
- [x] 0.7 Verify: 컴파일 확인

## Plan Step 1: Service Layer (TDD)

### AuthService.loginAdmin()
- [x] 1.1 RED: TC-AUTH-001 (로그인 성공) 테스트 작성 → 실패 확인
- [x] 1.2 GREEN: loginAdmin() 구현 → 테스트 통과
- [x] 1.3 RED: TC-AUTH-002 (매장 없음) 테스트 작성 → 실패 확인
- [x] 1.4 GREEN: 매장 검증 로직 → 테스트 통과
- [x] 1.5 RED: TC-AUTH-003 (사용자 없음) 테스트 작성 → 실패 확인
- [x] 1.6 GREEN: 사용자 검증 로직 → 테스트 통과
- [x] 1.7 RED: TC-AUTH-004 (비밀번호 불일치) 테스트 작성 → 실패 확인
- [x] 1.8 GREEN: 비밀번호 검증 로직 → 테스트 통과
- [x] 1.9 REFACTOR + VERIFY: 전체 loginAdmin 리팩토링, 모든 테스트 통과 확인

### AuthService.registerAdmin()
- [x] 1.10 RED: TC-AUTH-005 (등록 성공) 테스트 작성 → 실패 확인
- [x] 1.11 GREEN: registerAdmin() 구현 → 테스트 통과
- [x] 1.12 RED: TC-AUTH-006 (매장 없음) 테스트 작성 → 실패 확인
- [x] 1.13 GREEN: 매장 검증 로직 → 테스트 통과
- [x] 1.14 RED: TC-AUTH-007 (중복 username) 테스트 작성 → 실패 확인
- [x] 1.15 GREEN: 중복 검증 로직 → 테스트 통과
- [x] 1.16 REFACTOR + VERIFY: 전체 registerAdmin 리팩토링, 모든 테스트 통과 확인

### AuthService.loginTable()
- [x] 1.17 RED: TC-AUTH-008 (테이블 인증 성공) 테스트 작성 → 실패 확인
- [x] 1.18 GREEN: loginTable() 구현 → 테스트 통과
- [x] 1.19 RED: TC-AUTH-009 (매장 없음) 테스트 작성 → 실패 확인
- [x] 1.20 GREEN: 매장 검증 로직 → 테스트 통과
- [x] 1.21 RED: TC-AUTH-010 (테이블 없음) 테스트 작성 → 실패 확인
- [x] 1.22 GREEN: 테이블 검증 로직 → 테스트 통과
- [x] 1.23 RED: TC-AUTH-011 (비밀번호 불일치) 테스트 작성 → 실패 확인
- [x] 1.24 GREEN: 비밀번호 검증 로직 → 테스트 통과
- [x] 1.25 REFACTOR + VERIFY: 전체 loginTable 리팩토링, 모든 테스트 통과 확인

## Plan Step 2: Controller Layer (TDD)

### AdminAuthController
- [x] 2.1 RED: TC-AUTH-012 (login 성공 200) 테스트 작성 → 실패 확인
- [x] 2.2 GREEN: login() 엔드포인트 구현 → 테스트 통과
- [x] 2.3 RED: TC-AUTH-013 (login 실패 401) 테스트 작성 → 실패 확인
- [x] 2.4 GREEN: 에러 핸들링 → 테스트 통과
- [x] 2.5 RED: TC-AUTH-014 (register 성공 200) 테스트 작성 → 실패 확인
- [x] 2.6 GREEN: register() 엔드포인트 구현 → 테스트 통과
- [x] 2.7 RED: TC-AUTH-015 (register validation 400) 테스트 작성 → 실패 확인
- [x] 2.8 GREEN: validation 처리 → 테스트 통과
- [x] 2.9 REFACTOR + VERIFY: AdminAuthController 리팩토링, 모든 테스트 통과 확인

### TableAuthController
- [x] 2.10 RED: TC-AUTH-016 (login 성공 200) 테스트 작성 → 실패 확인
- [x] 2.11 GREEN: login() 엔드포인트 구현 → 테스트 통과
- [x] 2.12 RED: TC-AUTH-017 (login 실패 401) 테스트 작성 → 실패 확인
- [x] 2.13 GREEN: 에러 핸들링 → 테스트 통과
- [x] 2.14 REFACTOR + VERIFY: TableAuthController 리팩토링, 모든 테스트 통과 확인

## Plan Step 3: Seed Data + Documentation
- [x] 3.1 init.sql에 seed 데이터 추가 (매장 + 관리자 + 테이블)
- [x] 3.2 Code summary 문서 생성 (aidlc-docs/construction/unit-1-be-auth/code/code-summary.md)
