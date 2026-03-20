# Unit 1-BE Auth API - Functional Design 확인 질문

답변 분석 중 아래 사항에 대한 확인이 필요합니다.

---

## Clarification 1: 관리자 로그인 - username 유일성 범위
Q2에서 "사용자명 + 비밀번호만으로 검증 (매장 코드는 로그인 후 선택)"을 선택하셨습니다.
현재 DB 스키마는 `UNIQUE(store_id, username)` — 매장 내에서만 username이 유일합니다.

Q2 방식이면 username이 시스템 전체에서 유일해야 로그인 시 어떤 관리자인지 특정할 수 있습니다.

### Clarification Question 1
관리자 username 유일성 범위를 어떻게 할까요?

A) username을 시스템 전체에서 유일하게 변경 (UNIQUE 제약조건 수정) — Q2 답변과 일관
B) Q2 답변을 A로 변경 (매장 코드 + 사용자명 + 비밀번호 3단계 검증) — 현재 DB 스키마 유지
C) Other (please describe after [Answer]: tag below)

[Answer]: B

## Clarification 2: QR 일회용 토큰 저장 방식
Q5에서 "QR URL에 일회용 토큰 포함 → 비밀번호 없이 자동 인증"을 선택하셨습니다.
일회용 토큰 생성/검증/만료 관리가 필요합니다.

### Clarification Question 2
QR 일회용 토큰 저장 및 관리 방식은?

A) DB 테이블 추가 (qr_tokens: token, store_id, table_id, expires_at, used)
B) 인메모리 (ConcurrentHashMap) — 서버 재시작 시 초기화되지만 MVP에 적합
C) JWT 자체를 QR 토큰으로 사용 (storeCode + tableNo를 클레임에 포함, 짧은 만료시간) — 별도 저장 불필요
D) Other (please describe after [Answer]: tag below)

[Answer]: C

## Clarification 3: 매장+관리자 동시 등록 API 범위
Q6에서 "매장 등록 시 관리자 계정도 함께 생성"을 선택하셨습니다.
이 API는 매장(Store) 도메인과 인증(Auth) 도메인에 걸쳐 있습니다.

### Clarification Question 3
매장+관리자 동시 등록 API를 어느 Unit에 배치할까요?

A) Unit 1-BE (Auth)에 배치 — Store 엔티티도 Unit 1에서 함께 생성
B) Unit 2-BE (Store/Table)에 배치 — Unit 1은 로그인/인증만, 매장+관리자 등록은 Unit 2에서
C) Other (please describe after [Answer]: tag below)

[Answer]: B
