# Code Summary - Unit 0 (Foundation)

## 생성된 파일 목록

### 프로젝트 루트
- `table-order/docker-compose.yml` - Docker Compose (mysql, backend, customer, admin)
- `table-order/.gitignore`

### Backend (Spring Boot)
- `backend/build.gradle` - Spring Boot 3, JPA, Security, JWT
- `backend/settings.gradle`
- `backend/Dockerfile`
- `backend/gradle/wrapper/gradle-wrapper.properties`
- `backend/src/main/resources/application.yml`
- `backend/src/main/resources/db/init.sql` - MySQL 9테이블 DDL
- `backend/src/main/java/com/tableorder/TableOrderApplication.java`
- `backend/src/main/java/com/tableorder/common/dto/ApiResponse.java`
- `backend/src/main/java/com/tableorder/common/exception/ErrorCode.java`
- `backend/src/main/java/com/tableorder/common/exception/BusinessException.java`
- `backend/src/main/java/com/tableorder/common/exception/GlobalExceptionHandler.java`
- `backend/src/main/java/com/tableorder/common/config/SecurityConfig.java`
- `backend/src/main/java/com/tableorder/common/config/JwtTokenProvider.java`
- `backend/src/main/java/com/tableorder/common/config/JwtAuthenticationFilter.java`
- `backend/src/main/java/com/tableorder/common/config/WebConfig.java`
- `backend/src/test/java/com/tableorder/TableOrderApplicationTests.java`
- `backend/src/test/resources/application-test.yml`
- 도메인 패키지 구조: auth/, store/, menu/, order/ (각 domain/controller/service/repository/dto)

### Frontend (Turborepo 모노레포)
- `frontend/package.json`, `pnpm-workspace.yaml`, `turbo.json`, `tsconfig.json`, `.gitignore`

#### @table-order/shared
- `packages/shared/src/constants/order-status.ts`, `config.ts`
- `packages/shared/src/utils/format.ts`, `storage.ts`
- `packages/shared/src/index.ts`

#### @table-order/api-client
- `packages/api-client/src/client.ts` - HTTP 클라이언트 (GET/POST/PUT/PATCH/DELETE/upload)
- `packages/api-client/src/sse.ts` - SSE 연결 헬퍼 (자동 재연결)
- `packages/api-client/src/types/common.ts`
- `packages/api-client/src/index.ts`

#### @table-order/ui
- Button, Card, Modal, Input, Badge, Spinner, Toast, EmptyState, ConfirmDialog (9개 컴포넌트)
- `packages/ui/src/index.ts`

#### 고객앱 (apps/customer)
- `package.json`, `next.config.ts`, `tsconfig.json`, `postcss.config.js`, `Dockerfile`
- `app/globals.css`, `app/layout.tsx`, `app/providers.tsx`, `app/page.tsx`

#### 관리자앱 (apps/admin)
- `package.json`, `next.config.ts`, `tsconfig.json`, `postcss.config.js`, `Dockerfile`
- `app/globals.css`, `app/layout.tsx`, `app/providers.tsx`, `app/page.tsx`
