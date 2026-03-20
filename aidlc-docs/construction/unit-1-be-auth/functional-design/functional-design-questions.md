# Unit 1-BE Auth API - Functional Design 질문

Unit 1-BE (Auth API) 상세 비즈니스 로직 설계를 위한 질문입니다.
각 질문의 [Answer]: 뒤에 선택지 알파벳을 입력해주세요.

---

## Question 1
관리자 로그인 시도 횟수 제한 정책은 어떻게 설정할까요?

A) 5회 실패 시 15분 잠금 (일반적)
B) 3회 실패 시 30분 잠금 (보안 강화)
C) 10회 실패 시 5분 잠금 (사용자 편의)
D) 시도 횟수 제한 없음 (MVP 단순화)
E) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 2
관리자 로그인 시 매장 코드(storeCode) 검증 방식은?

A) 매장 코드 + 사용자명 + 비밀번호 3단계 검증 (설계대로)
B) 사용자명 + 비밀번호만 검증 (매장 코드는 로그인 후 선택)
C) Other (please describe after [Answer]: tag below)

[Answer]: B

## Question 3
테이블 인증(태블릿 자동 로그인) 시 JWT에 포함할 클레임 정보는?

A) storeId, tableId, tableNo, role=TABLE (최소 정보)
B) storeId, tableId, tableNo, storeName, role=TABLE (매장명 포함)
C) storeId, tableId, tableNo, sessionId, role=TABLE (세션 ID 포함)
D) Other (please describe after [Answer]: tag below)

[Answer]: B

## Question 4
관리자 JWT에 포함할 클레임 정보는?

A) storeId, adminId, username, role=ADMIN (기본)
B) storeId, adminId, username, storeName, role=ADMIN (매장명 포함)
C) Other (please describe after [Answer]: tag below)

[Answer]: B

## Question 5
QR 코드 모바일 웹 접근 시 인증 방식은?

A) QR URL에 storeCode + tableNo 포함 → 별도 비밀번호 입력 필요
B) QR URL에 storeCode + tableNo + 일회용 토큰 포함 → 비밀번호 없이 자동 인증
C) QR URL에 storeCode + tableNo 포함 → 태블릿과 동일한 비밀번호 입력
D) Other (please describe after [Answer]: tag below)

[Answer]: B

## Question 6
관리자 계정 생성(회원가입) API가 Unit 1에 필요한가요?

A) 필요 — 관리자가 직접 회원가입 (매장 등록은 Unit 2에서)
B) 필요 — 매장 등록 시 관리자 계정도 함께 생성 (매장+관리자 동시 등록 API)
C) 불필요 — init.sql 또는 시드 데이터로 관리자 계정 사전 생성 (MVP 단순화)
D) Other (please describe after [Answer]: tag below)

[Answer]: B

## Question 7
로그인 시도 횟수 추적 방식은? (Question 1에서 D를 선택한 경우 무시)

A) 인메모리 (ConcurrentHashMap) — 서버 재시작 시 초기화
B) DB 저장 (admins 테이블에 login_attempts, locked_until 컬럼 추가)
C) Other (please describe after [Answer]: tag below)

[Answer]: B
