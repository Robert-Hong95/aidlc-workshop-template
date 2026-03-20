# Unit 2-BE Store/Table API - NFR Requirements 질문

Unit 1-BE에서 결정된 공통 NFR (JWT, CORS, 로깅, 비밀번호 정책 등)은 그대로 적용합니다.
Unit 2-BE에 특화된 추가 NFR만 확인합니다.

---

## Question 1
매장 등록 API (POST /api/stores)는 인증 없이 호출 가능합니다. Rate Limiting이 필요한가요?

A) 필요 없음 (MVP, 내부 사용 목적)
B) IP 기반 Rate Limiting (예: 분당 10회)
C) Other (please describe after [Answer]: tag below)

[Answer]: B

## Question 2
테이블 이용 완료(endSession) 시 주문 JSON 직렬화+삭제가 트랜잭션으로 묶입니다. 대량 주문 시 타임아웃 전략은?

A) 단일 트랜잭션, 타임아웃 30초 (MVP 충분)
B) 배치 처리 (100건씩 나눠서 처리)
C) Other (please describe after [Answer]: tag below)

[Answer]: B

## Question 3
과거 주문 내역(order_history) 데이터 보관 기간은?

A) 무기한 보관
B) 6개월 보관 후 자동 삭제
C) 1년 보관 후 자동 삭제
D) Other (please describe after [Answer]: tag below)

[Answer]: C 

## Question 4
매장 등록 시 store_code 형식 검증이 필요한가요?

A) 영문+숫자만 허용, 4~20자
B) 형식 제한 없음 (빈 값만 아니면 됨)
C) Other (please describe after [Answer]: tag below)

[Answer]: B

## Question 5
테이블 목록 조회 시 활성 세션 정보를 함께 반환합니다. 조회 성능 전략은?

A) JOIN 쿼리로 한 번에 조회
B) 테이블 목록 조회 후 세션 별도 조회 (N+1이지만 MVP 단순화)
C) Other (please describe after [Answer]: tag below)

[Answer]: A
