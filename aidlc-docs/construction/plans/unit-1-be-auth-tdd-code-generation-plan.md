# TDD Code Generation Plan - Unit 1-BE Auth API

## Unit Context
- **Workspace Root**: /Users/wm-it-22-00429/Wemade/workshop/aidlc-workshop-template
- **Project Type**: Greenfield (Unit 0 Foundation 완료)
- **Code Root**: table-order/backend/src/main/java/com/tableorder/
- **Test Root**: table-order/backend/src/test/java/com/tableorder/
- **Stories**: US-A01 (매장 인증), US-C01 (테이블 자동 로그인)

---

### Plan Step 0: Contract Skeleton + DB Schema Update
- [x] 0.1: admins 테이블에 login_attempts, locked_until 컬럼 추가 (init.sql 수정)
- [x] 0.2: Admin 엔티티 생성 (auth/domain/Admin.java)
- [x] 0.3: Store 엔티티 생성 (store/domain/Store.java)
- [x] 0.4: StoreTable 엔티티 생성 (store/domain/StoreTable.java)
- [x] 0.5: Repository 인터페이스 생성 (AdminRepository, StoreRepository, StoreTableRepository)
- [x] 0.6: DTO 생성 (LoginRequest, TableLoginRequest, QrLoginRequest, QrTokenRequest, TokenResponse, QrTokenResponse, AuthTokens)
- [x] 0.7: AuthService 스켈레톤 → 구현 완료
- [x] 0.8: Controller 생성 (AdminAuthController, TableAuthController, AuthCommonController, QrTokenController)
- [x] 0.9: JwtTokenProvider 수정 (createAccessToken, createRefreshToken, createQrToken 분리)
- [x] 0.10: JwtAuthenticationFilter 수정 (tokenType=ACCESS만 인증)
- [x] 0.11: SecurityConfig 수정 (새 엔드포인트 permitAll)
- [x] 0.12: application.yml, application-test.yml 업데이트

### Plan Step 1: Domain Layer (TDD)
- [x] 1.1: Admin.isLocked() — TC-AUTH-001, 002
- [x] 1.2: Admin.incrementLoginAttempts() — TC-AUTH-003, 004
- [x] 1.3: Admin.resetLoginAttempts() — TC-AUTH-005

### Plan Step 2: Service Layer (TDD)
- [x] 2.1: AuthService.loginAdmin() — TC-AUTH-006~012
- [x] 2.2: AuthService.loginTable() — TC-AUTH-013~015
- [x] 2.3: AuthService.loginByQrToken() — TC-AUTH-016~018
- [x] 2.4: AuthService.generateQrToken() — TC-AUTH-019
- [x] 2.5: AuthService.refreshToken() — TC-AUTH-020~022

### Plan Step 3: Controller Layer (TDD)
- [x] 3.1: AdminAuthController — 구현 완료
- [x] 3.2: TableAuthController — 구현 완료
- [x] 3.3: AuthCommonController — 구현 완료

### Plan Step 4: Modified Components (TDD)
- [x] 4.1: JwtTokenProvider — TC-AUTH-030~032

### Plan Step 5: Final Verification
- [x] 5.1: 전체 테스트 작성 완료
- [x] 5.2: 코드 문서 생성
