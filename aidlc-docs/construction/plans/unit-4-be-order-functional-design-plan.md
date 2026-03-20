# Functional Design Plan - Unit 4-BE (Order + SSE API)

## Unit 정보
- **Unit**: Unit 4-BE - 주문 + SSE (Order)
- **Stories**: US-C04 (주문 생성), US-C05 (주문 내역), US-A02 (실시간 모니터링), US-S02 (SSE)
- **Services**: OrderService, SseEmitterService
- **Controllers**: CustomerOrderController, AdminOrderController, CustomerSseController, AdminSseController
- **추가**: TableService.endSession() 주문→이력 이관 로직 완성

## Plan Steps
- [x] Step 1: 비즈니스 질문 수집 및 답변
- [x] Step 2: Functional Design 문서 생성

---

## Step 1: 비즈니스 질문

### Q1. 주문 상태 전이
주문 상태 흐름:
- A) PENDING → PREPARING → COMPLETED (3단계)
- B) PENDING → ACCEPTED → PREPARING → COMPLETED (4단계)
- C) 직접 지정: ___

[Answer]: A

### Q2. 주문 삭제 권한
- A) 관리자만 삭제 가능
- B) 고객도 PENDING 상태에서 취소 가능
- C) 둘 다 가능 (관리자는 모든 상태, 고객은 PENDING만)

[Answer]: C

### Q3. SSE Heartbeat 간격
- A) 15초
- B) 30초
- C) 60초

[Answer]: A

### Q4. SSE 연결 타임아웃
- A) 10분
- B) 30분
- C) 60분

[Answer]: A

### Q5. 주문 생성 시 세션 처리
활성 세션이 없는 테이블에서 주문 시:
- A) 자동으로 새 세션 생성
- B) 에러 반환 (세션 없음)

[Answer]: A

### Q6. endSession 시 주문 상태 제한
PENDING/PREPARING 상태 주문이 있을 때 세션 종료:
- A) 모든 주문을 강제 COMPLETED 처리 후 이관
- B) 미완료 주문이 있으면 세션 종료 차단
- C) 상태 무관하게 그대로 이관

[Answer]: B

