# Unit 2-BE Store/Table API - Functional Design 질문

Unit 2-BE (Store/Table API) 상세 비즈니스 로직 설계를 위한 질문입니다.
각 질문의 [Answer]: 뒤에 선택지 알파벳을 입력해주세요.

---

## Question 1
매장 등록 시 관리자 계정도 함께 생성하는 API 설계는? (이전 세션에서 Q6=B로 결정)

A) 단일 API: POST /api/stores — body에 매장 정보 + 관리자 정보 포함, 트랜잭션으로 동시 생성
B) 2단계 API: POST /api/stores → POST /api/stores/{id}/admins — 매장 먼저, 관리자 별도
C) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 2
매장 정보에 포함할 필드는? (현재 DB: store_code, name)

A) 현재 스키마 유지 (store_code, name만)
B) 추가 필드: address, phone, business_hours (운영 정보)
C) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 3
테이블 초기 설정 시 세션 자동 시작 여부는?

A) 테이블 설정 시 세션 자동 시작 (설정 즉시 사용 가능)
B) 테이블 설정만 하고, 고객 첫 주문 시 세션 시작
C) 테이블 설정만 하고, 관리자가 수동으로 세션 시작
D) Other (please describe after [Answer]: tag below)

[Answer]: B

## Question 4
테이블 이용 완료(세션 종료) 시 주문 이력 이동 방식은?

A) 주문 데이터를 JSON으로 직렬화하여 order_history에 저장 후 orders에서 삭제
B) orders 테이블에 유지하되 status를 COMPLETED로 변경 (삭제 안 함)
C) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 5
과거 주문 내역 조회 시 페이지네이션 방식은?

A) Offset 기반 (page, size 파라미터)
B) Cursor 기반 (lastId 파라미터)
C) 전체 조회 (MVP 단순화, 데이터 적을 것으로 예상)
D) Other (please describe after [Answer]: tag below)

[Answer]: B

## Question 6
다중 매장 관리 시 관리자-매장 관계는?

A) 1:1 — 관리자 1명당 매장 1개 (현재 DB 구조)
B) N:1 — 관리자 여러 명이 매장 1개 관리 (현재 DB 구조로 가능)
C) N:M — 관리자 1명이 여러 매장 관리 가능 (중간 테이블 필요)
D) Other (please describe after [Answer]: tag below)

[Answer]: B
