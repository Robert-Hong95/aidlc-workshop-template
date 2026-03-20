# 테이블오더 로컬 실행 가이드

## 사전 요구사항

- Java 17+
- Node.js 18+
- pnpm 9+ (`npm install -g pnpm`)

## 실행 순서

### 1. 백엔드 실행 (먼저)

```bash
cd table-order/backend
./gradlew bootRun
```

- http://localhost:8080 에서 실행됨
- H2 인메모리 DB 사용 (별도 DB 설치 불필요)
- 시작 시 초기 데이터 자동 생성 (매장, 테이블, 메뉴, 이미지)

### 2. 프론트엔드 실행

```bash
cd table-order/frontend
pnpm install
pnpm dev
```

- Customer 앱: http://localhost:3000
- Admin 앱: http://localhost:3001

## 초기 계정 정보

| 구분 | 매장코드 | 아이디/테이블번호 | 비밀번호 |
|------|----------|-------------------|----------|
| 관리자 | STORE01 | admin | pass1234 |
| 테이블 1~4 | STORE01 | 1, 2, 3, 4 | pass1234 |

## 주의사항

- 반드시 백엔드를 먼저 실행한 후 프론트엔드를 실행하세요
- 백엔드 재시작 시 H2 인메모리 DB가 초기화됩니다 (데이터 리셋)
- `.env.local` 파일로 API URL을 개인 환경에 맞게 오버라이드할 수 있습니다
