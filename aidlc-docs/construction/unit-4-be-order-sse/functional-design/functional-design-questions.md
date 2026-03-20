# Unit 4-BE Order+SSE API - Functional Design 질문

Unit 4-BE (Order+SSE API) 상세 비즈니스 로직 설계를 위한 질문입니다.
각 질문의 [Answer]: 뒤에 선택지 알파벳을 입력해주세요.

---

## Question 1
주문 상태 흐름은?

A) PENDING → PREPARING → COMPLETED (3단계)
B) PENDING → CONFIRMED → PREPARING → COMPLETED (4단계)
C) Other (please describe after [Answer]: tag below)

[Answer]: B

## Question 2
주문 생성 시 세션 자동 시작 (Unit 2에서 결정: 고객 첫 주문 시 세션 시작) 구현 위치는?

A) OrderService.createOrder() 내에서 TableService.startSession() 호출
B) OrderService.createOrder()에서 직접 세션 생성 로직 포함
C) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 3
SSE 연결 타임아웃은?

A) 30초 (짧은 주기, 클라이언트 자동 재연결)
B) 5분
C) 30분
D) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 4
주문 삭제 권한은?

A) 관리자만 삭제 가능
B) 관리자 + 고객 본인 (PENDING 상태일 때만)
C) Other (please describe after [Answer]: tag below)

[Answer]: B

## Question 5
고객 주문 내역 조회 범위는?

A) 현재 세션의 주문만 조회
B) 현재 세션 + 과거 세션 모두 조회
C) Other (please describe after [Answer]: tag below)

[Answer]: A
