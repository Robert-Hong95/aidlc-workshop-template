# Code Generation Plan - Unit 0 (Foundation)

## Unit Context
- **Unit**: Unit 0 - Foundation (프로젝트 초기 설정)
- **Stories**: 없음 (인프라 스캐폴딩)
- **Dependencies**: 없음 (최초 Unit)
- **TDD**: A (이후 Unit부터 본격 적용, Unit 0은 설정 코드 위주)

## Code Location
- **Backend**: `table-order/backend/`
- **Frontend**: `table-order/frontend/`
- **Docker**: `table-order/`
- **Documentation**: `aidlc-docs/construction/unit-0-foundation/code/`

---

## Generation Steps

### Step 1: 프로젝트 루트 구조
- [x] `table-order/` 루트 디렉토리 생성
- [x] `table-order/docker-compose.yml` 생성
- [x] `table-order/.gitignore` 생성

### Step 2: Backend - Spring Boot 프로젝트 초기화
- [x] `table-order/backend/build.gradle` 생성 (Spring Boot 3, JPA, MySQL, Security, Web, Validation)
- [x] `table-order/backend/settings.gradle` 생성
- [x] `table-order/backend/src/main/resources/application.yml` 생성
- [x] `table-order/backend/src/main/java/com/tableorder/TableOrderApplication.java` 생성
- [x] `table-order/backend/Dockerfile` 생성

### Step 3: Backend - 공통 모듈 (common)
- [x] `ApiResponse.java` - 공통 API 응답 래퍼
- [x] `ErrorCode.java` - 에러 코드 enum
- [x] `BusinessException.java` - 비즈니스 예외
- [x] `GlobalExceptionHandler.java` - 글로벌 예외 핸들러

### Step 4: Backend - Security 설정
- [x] `SecurityConfig.java` - Spring Security 설정 (CORS, 필터 체인)
- [x] `JwtTokenProvider.java` - JWT 토큰 생성/검증
- [x] `JwtAuthenticationFilter.java` - JWT 인증 필터
- [x] `WebConfig.java` - CORS 설정

### Step 5: Backend - 도메인 패키지 구조 (빈 패키지)
- [x] auth/ 도메인 패키지 구조 생성 (domain, controller, service, repository, dto)
- [x] store/ 도메인 패키지 구조 생성
- [x] menu/ 도메인 패키지 구조 생성
- [x] order/ 도메인 패키지 구조 생성

### Step 6: Backend - DB 초기화 스크립트
- [x] `table-order/backend/src/main/resources/db/init.sql` - 전체 테이블 DDL

### Step 7: Backend - 테스트 설정
- [x] `table-order/backend/src/test/java/com/tableorder/TableOrderApplicationTests.java`
- [x] `table-order/backend/src/test/resources/application-test.yml`

### Step 8: Frontend - Turborepo 모노레포 초기화
- [x] `table-order/frontend/package.json` (루트)
- [x] `table-order/frontend/pnpm-workspace.yaml`
- [x] `table-order/frontend/turbo.json`
- [x] `table-order/frontend/tsconfig.json` (공유 TS 설정)
- [x] `table-order/frontend/.gitignore`

### Step 9: Frontend - 공유 패키지 (@table-order/shared)
- [x] `packages/shared/package.json`
- [x] `packages/shared/tsconfig.json`
- [x] `packages/shared/src/constants/order-status.ts`
- [x] `packages/shared/src/constants/config.ts`
- [x] `packages/shared/src/utils/format.ts`
- [x] `packages/shared/src/utils/storage.ts`
- [x] `packages/shared/src/index.ts`

### Step 10: Frontend - 공유 패키지 (@table-order/api-client)
- [x] `packages/api-client/package.json`
- [x] `packages/api-client/tsconfig.json`
- [x] `packages/api-client/src/client.ts` (기본 HTTP 클라이언트)
- [x] `packages/api-client/src/sse.ts` (SSE 연결 헬퍼)
- [x] `packages/api-client/src/types/common.ts`
- [x] `packages/api-client/src/index.ts`

### Step 11: Frontend - 공유 패키지 (@table-order/ui)
- [x] `packages/ui/package.json`
- [x] `packages/ui/tsconfig.json`
- [x] `packages/ui/src/components/Button.tsx`
- [x] `packages/ui/src/components/Card.tsx`
- [x] `packages/ui/src/components/Modal.tsx`
- [x] `packages/ui/src/components/Input.tsx`
- [x] `packages/ui/src/components/Badge.tsx`
- [x] `packages/ui/src/components/Spinner.tsx`
- [x] `packages/ui/src/components/Toast.tsx`
- [x] `packages/ui/src/components/EmptyState.tsx`
- [x] `packages/ui/src/components/ConfirmDialog.tsx`
- [x] `packages/ui/src/index.ts`

### Step 12: Frontend - 고객앱 (apps/customer) 초기화
- [x] `apps/customer/package.json`
- [x] `apps/customer/next.config.ts`
- [x] `apps/customer/tsconfig.json`
- [x] `apps/customer/tailwind.config.ts`
- [x] `apps/customer/postcss.config.js`
- [x] `apps/customer/app/layout.tsx` (Root Layout + Provider)
- [x] `apps/customer/app/page.tsx` (메뉴 페이지 placeholder)
- [x] `apps/customer/Dockerfile`

### Step 13: Frontend - 관리자앱 (apps/admin) 초기화
- [x] `apps/admin/package.json`
- [x] `apps/admin/next.config.ts`
- [x] `apps/admin/tsconfig.json`
- [x] `apps/admin/tailwind.config.ts`
- [x] `apps/admin/postcss.config.js`
- [x] `apps/admin/app/layout.tsx` (Root Layout + Provider)
- [x] `apps/admin/app/page.tsx` (대시보드 placeholder)
- [x] `apps/admin/Dockerfile`

### Step 14: Documentation
- [x] `aidlc-docs/construction/unit-0-foundation/code/code-summary.md` 생성
