# Unit 1-BE Auth API - Functional Design Plan

## Unit 정보
- **Unit**: Unit 1-BE (Auth API)
- **Stories**: US-A01 (매장 인증), US-C01 (테이블 자동 로그인)
- **범위**: Backend only (table-order/backend/)

## 설계 계획

- [x] Step 1: 질문 수집 및 분석
- [x] Step 2: Domain Entity 상세 설계 (Admin 엔티티)
- [x] Step 3: Business Logic 상세 설계 (AuthService - 관리자 로그인, 테이블 인증)
- [x] Step 4: Business Rules 정의 (비밀번호 검증, JWT 발급, 로그인 시도 제한)
- [x] Step 5: DTO 설계 (Request/Response)
- [x] Step 6: Controller API 엔드포인트 설계 (AdminAuthController, TableAuthController)
- [x] Step 7: 산출물 파일 생성

## 참조 산출물
- `inception/application-design/component-methods.md` — AuthService 메서드 시그니처
- `inception/application-design/services.md` — 관리자/테이블 인증 플로우
- `inception/user-stories/stories.md` — US-A01, US-C01 Acceptance Criteria
- `construction/unit-0-foundation/infrastructure-design/` — DB 스키마, JWT 설정
- Unit 0 코드: SecurityConfig, JwtTokenProvider, JwtAuthenticationFilter, ErrorCode

## 확정된 설계 결정
- Q2→CQ1: 매장 코드 + 사용자명 + 비밀번호 3단계 검증 (DB 스키마 유지)
- Q5→CQ2: JWT 자체를 QR 토큰으로 사용 (별도 저장 불필요)
- Q6→CQ3: 매장+관리자 등록은 Unit 2에서, Unit 1은 로그인/인증만
