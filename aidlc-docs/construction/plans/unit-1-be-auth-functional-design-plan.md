# Functional Design Plan - Unit 1-BE (Auth API)

## 대상 Stories
- US-A01: 매장 인증 (관리자 로그인/로그아웃, JWT 16시간 세션)
- US-C01: 테이블 자동 로그인 (테이블 인증, JWT 발급)

## 기존 Foundation 코드 (Unit 0)
- JwtTokenProvider: 토큰 생성/검증/파싱 구현 완료
- JwtAuthenticationFilter: Bearer 토큰 추출, role 기반 인증 설정 완료
- SecurityConfig: 엔드포인트별 인증 규칙, BCryptPasswordEncoder 설정 완료
- DB 스키마: admins, stores, store_tables 테이블 정의 완료

## 설계 항목

- [x] 1. Domain Entities 설계 (Admin 엔티티)
- [x] 2. Business Logic Model 설계 (관리자 로그인, 테이블 인증 플로우)
- [x] 3. Business Rules 설계 (검증 규칙, 로그인 시도 제한, 토큰 클레임)

---

# Functional Design 질문

아래 질문에 답변해 주세요.

## Question 1
관리자 로그인 시도 제한 정책을 어떻게 설정하시겠습니까?

A) 5회 실패 시 15분 잠금 (인메모리 카운터)
B) 10회 실패 시 30분 잠금 (인메모리 카운터)
C) 로그인 시도 제한 없이 MVP 진행 (향후 추가)
D) Other (please describe after [Answer]: tag below)

[Answer]: C

## Question 2
관리자 초기 계정은 어떻게 생성하시겠습니까?

A) init.sql에 seed 데이터로 삽입 (매장 + 관리자 계정 함께)
B) 별도의 관리자 등록 API 제공 (/api/admin/auth/register)
C) 둘 다 (seed 데이터 + 등록 API)
D) Other (please describe after [Answer]: tag below)

[Answer]: c

## Question 3
테이블 인증 JWT 토큰의 만료 시간은 어떻게 설정하시겠습니까? (관리자는 16시간으로 확정)

A) 관리자와 동일하게 16시간
B) 24시간 (태블릿은 하루 종일 사용)
C) 만료 없음 (태블릿은 항상 로그인 상태 유지)
D) Other (please describe after [Answer]: tag below)

[Answer]: C

## Question 4
JWT 토큰에 포함할 클레임(claims) 정보는 어떤 것이 필요합니까?

A) 최소한: role, storeId만 포함 (나머지는 API 호출 시 조회)
B) 표준: role, storeId, tableId(테이블용), username(관리자용)
C) 확장: role, storeId, tableId, tableNo, storeName, username
D) Other (please describe after [Answer]: tag below)

[Answer]: B

## Question 5
관리자 로그아웃 처리 방식은 어떻게 하시겠습니까?

A) 클라이언트 측에서만 토큰 삭제 (서버 측 블랙리스트 없음)
B) 서버 측 토큰 블랙리스트 관리 (Redis 또는 인메모리)
C) Other (please describe after [Answer]: tag below)

[Answer]: A
