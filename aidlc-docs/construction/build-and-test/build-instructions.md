# Build Instructions - 테이블오더 서비스

## 사전 요구사항

| 도구 | 버전 | 용도 |
|------|------|------|
| Docker & Docker Compose | 최신 | 전체 서비스 실행 |
| Java 17+ | 17 | Backend 빌드 (로컬) |
| Node.js 20+ | 20 | Frontend 빌드 (로컬) |
| pnpm 9+ | 9.15.0 | Frontend 패키지 관리 |

## 1. Docker Compose로 전체 실행 (권장)

```bash
cd table-order/
docker compose up --build -d
```

서비스 접속:
- 고객앱: http://localhost:3000
- 관리자앱: http://localhost:3001
- Backend API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html

종료:
```bash
docker compose down
```

데이터 초기화 포함 종료:
```bash
docker compose down -v
```

## 2. 로컬 개별 빌드

### Backend

```bash
cd table-order/backend/
./gradlew build -x test    # 빌드 (테스트 제외)
./gradlew build             # 빌드 + 테스트
./gradlew bootRun           # 실행 (MySQL 필요)
```

MySQL 로컬 실행 (Docker):
```bash
docker compose up mysql -d
```

### Frontend

```bash
cd table-order/frontend/
pnpm install
pnpm run build              # 전체 빌드 (admin + customer)
pnpm run dev                # 개발 서버 실행
```

## 3. Seed Data

`init.sql`이 MySQL 초기화 시 자동 실행됩니다.

| 항목 | 값 |
|------|-----|
| 매장 코드 | STORE01 |
| 관리자 계정 | admin / pass1234 |
| 테이블 1 비밀번호 | pass1234 |
| 테이블 2 비밀번호 | pass1234 |

## 4. 환경 변수

| 변수 | 기본값 | 설명 |
|------|--------|------|
| NEXT_PUBLIC_API_URL | http://localhost:8080 | Frontend → Backend API URL |
| SPRING_DATASOURCE_URL | jdbc:mysql://localhost:3306/table_order | DB 접속 URL |
| JWT_SECRET | (내장) | JWT 서명 키 |
| FILE_UPLOAD_DIR | ./uploads | 이미지 업로드 경로 |
